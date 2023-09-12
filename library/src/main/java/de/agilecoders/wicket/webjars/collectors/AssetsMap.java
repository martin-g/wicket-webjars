package de.agilecoders.wicket.webjars.collectors;

import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import de.agilecoders.wicket.webjars.util.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static de.agilecoders.wicket.webjars.util.Helper.reversePath;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * asset holder map.
 *
 * @author miha
 */
public class AssetsMap implements IAssetProvider, IRecentVersionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(WicketWebjars.class);

    private final IWebjarsSettings settings;
    private final SortedMap<String, String> fullPathIndex;
    private final AssetPathCollector[] collectors;
    private final String recentVersionPlaceHolder;

    /**
     * Construct.
     *
     * @param settings the settings to use.
     */
    public AssetsMap(IWebjarsSettings settings) {
        this.settings = settings;
        this.collectors = settings.assetPathCollectors();
        this.recentVersionPlaceHolder = settings.recentVersionPlaceHolder();
        this.fullPathIndex = new TreeMap<>();
        reindex();
    }

    public AssetsMap reindex() {
        final Pattern resourcePattern = settings.resourcePattern();
        final ClassLoader[] classLoaders = settings.classLoaders();
        final SortedMap<String, String> _fullPathIndex = createFullPathIndex(resourcePattern, classLoaders);
        fullPathIndex.clear();
        fullPathIndex.putAll(_fullPathIndex);
        return this;
    }

    @Override
    public String findRecentVersionFor(String path) {
        final String partialPath = Helper.prependWebjarsPathIfMissing(path);
        final Matcher partialPathMatcher = settings.webjarsPathPattern().matcher(partialPath);

        if (partialPathMatcher.find() && recentVersionPlaceHolder.equalsIgnoreCase(partialPathMatcher.group(2))) {
            final Set<String> assets = listAssets(partialPathMatcher.group(1));
            final String fileName = "/" + partialPathMatcher.group(3);
            final Set<String> versions = new HashSet<>();

            for (String asset : assets) {
                if (asset.endsWith(fileName)) {
                    final Matcher matcher = settings.webjarsPathPattern().matcher(asset);

                    if (matcher.find()) {
                        versions.add(matcher.group(2));
                    }
                }
            }

            if (versions.size() == 1) {
                return versions.iterator().next();
            } else if (versions.size() > 1) {
                final String first = versions.iterator().next();
                LOG.warn("More than one version of a webjars resource has been found in the classpath. " +
                    "This is not supported! Webjars resource: {}; available versions: {}; going to use: {}",
                        fileName, versions, first);

                return first;
            } else {
                LOG.debug("No version found for webjars resource: {}", partialPath);
            }
        } else {
            LOG.trace("given webjars resource isn't a dynamic versioned one: {}", partialPath);
        }

        return null;
    }

    @Override
    public SortedMap<String, String> getFullPathIndex() {
        return fullPathIndex;
    }

    @Override
    public Set<String> listAssets(final String folderPath) {
        final Collection<String> allAssets = getFullPathIndex().values();

        final Set<String> assets = new HashSet<>();

        final String prefix;
        final String webjarsPath = settings.webjarsPath();
        if (webjarsPath.endsWith("/")) {
        	prefix = webjarsPath + Helper.removeLeadingSlash(folderPath);
        } else {
        	prefix = webjarsPath + Helper.prependLeadingSlash(folderPath);
        }
        
        for (final String asset : allAssets) {
            if (asset.startsWith(prefix)) {
                assets.add(asset);
            }
        }

        return assets;
    }

    /**
     * Return all {@link URL}s found in webjars directory,
     * either identifying JAR files or plain directories.
     */
    private Set<URL> listWebjarsParentURLs(final ClassLoader[] classLoaders) {
        final Set<URL> urls = new HashSet<>();
        final String webjarsPath = settings.webjarsPath();

        for (final ClassLoader classLoader : classLoaders) {
            try {
                final Enumeration<URL> enumeration = classLoader.getResources(webjarsPath);
                while (enumeration.hasMoreElements()) {
                    urls.add(enumeration.nextElement());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return urls;
    }

    /**
     * Return all of the resource paths filtered given an expression and a list of class loaders.
     */
    private Set<String> getAssetPaths(final Pattern filterExpr, final ClassLoader... classLoaders) {
        final Set<String> assetPaths = new HashSet<>();
        
        final Set<URL> urls = listWebjarsParentURLs(classLoaders);

        for (final URL url : urls) {
            for (AssetPathCollector collector : collectors) {
                if (collector.accept(url)) {
                    Collection<String> collection = collector.collect(url, filterExpr);
                    assetPaths.addAll(collection);
                }
            }
        }

        return assetPaths;
    }

    /**
     * Return a map that can be used to perform index lookups of partial file paths. This index constitutes a key that is the reverse form of the path
     * it relates to. Thus if a partial lookup needs to be performed from the rightmost path components then the key to access can be expressed easily
     * e.g. the path "a/b" would be the map tuple "b/a" -> "a/b". If we need to look for an asset named "a" without knowing the full path then we can
     * perform a partial lookup on the sorted map.
     *
     * @param filterExpr   the regular expression to be used to filter resources that will be included in the index.
     * @param classLoaders the class loaders to be considered for loading the resources from.
     * @return the index.
     */
    private SortedMap<String, String> createFullPathIndex(final Pattern filterExpr, final ClassLoader... classLoaders) {
        final Set<String> assetPaths = getAssetPaths(filterExpr, classLoaders);

        final SortedMap<String, String> assetPathIndex = new TreeMap<>();
        for (final String assetPath : assetPaths) {
            assetPathIndex.put(reversePath(assetPath), assetPath);
        }

        return assetPathIndex;
    }
}
