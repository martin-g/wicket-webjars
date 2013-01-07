package de.agilecoders.wicket.webjars.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import de.agilecoders.wicket.webjars.util.file.WebjarsResourceFinder;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.lang.Args;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.webjars.AssetLocator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for webjars resources
 *
 * @author miha
 */
public final class Webjars {

    private static final String PATH_SPLITTER = "/";

    private static final Pattern webjarsPattern = Pattern.compile("/webjars/([^/]*)/([^/]*)/(.*)");

    private static final class AssetLocatorConfigurationBuilder {
        private static ConfigurationBuilder instance = new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(StringUtils.join(AssetLocator.WEBJARS_PATH_PREFIX, "."), AssetLocator.class.getClassLoader()))
                .setScanners(new ResourcesScanner());
    }

    /**
     * prepends the webjars path if missing
     *
     * @param path the file name to check
     * @return file name that starts with "/webjars/"
     */
    public static String prependWebjarsPathIfMissing(final String path) {
        Args.notEmpty(path, "path");

        final String cleanedName = appendLeadingSlash(path);

        if (!path.contains("/webjars/")) {
            return "/webjars" + cleanedName;
        }

        return path;
    }

    /**
     * prepends a leading slash if there is none.
     *
     * @param path the path
     * @return path with leading slash
     */
    private static String appendLeadingSlash(final String path) {
        return path.startsWith(PATH_SPLITTER) ? path : PATH_SPLITTER + path;
    }

    /**
     * replaces the version string "current" with the recent available version
     *
     * @param path the full resource path
     */
    public static String useRecentVersion(final String path) {
        if (path != null && path.matches("/webjars/[^/]*/current/.*")) {
            return path.replaceFirst("/current/", "/" + recentVersion(path) + "/");
        }

        return path;
    }

    /**
     * returns recent version of given dependency
     *
     * @param partialPath the path of dependency
     * @return recent version
     */
    public static String recentVersion(final String partialPath) {
        final Reflections reflections = new Reflections(AssetLocatorConfigurationBuilder.instance);
        final Matcher partialPathMatcher = webjarsPattern.matcher(partialPath);

        if (partialPathMatcher.find() && "current".equalsIgnoreCase(partialPathMatcher.group(2))) {
            final List<String> versions = Lists.newArrayList();
            final String fileName = partialPathMatcher.group(3);

            // the map in the reflection store is just the file name so if the file being located doesn't contain a "/" then
            // a shortcut can be taken.  Otherwise the collection of multimap's values need to be searched.
            // Either way the first match is returned (if there is a match)
            for (final Multimap<String, String> paths : reflections.getStore().getStoreMap().values()) {
                for (final String path : paths.values()) {
                    final Matcher matcher = webjarsPattern.matcher(path);

                    if (matcher.find() && path.endsWith(fileName)) {
                        versions.add(matcher.group(2));
                    }
                }
            }

            if (versions.size() == 1) {
                return versions.get(0);
            } // more than one version of a dependency is not supported
        }

        return null;
    }

    /**
     * installs the webjars resource finder
     *
     * @param app the wicket application
     */
    public static void install(final Application app) {
        final List<IResourceFinder> finders = app.getResourceSettings().getResourceFinders();

        if (!finders.contains(WebjarsResourceFinder.instance())) {
            finders.add(WebjarsResourceFinder.instance());
        }
    }

    /**
     * private constructor.
     */
    private Webjars() {
        throw new UnsupportedOperationException();
    }
}
