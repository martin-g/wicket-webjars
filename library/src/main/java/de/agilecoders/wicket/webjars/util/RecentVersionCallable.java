package de.agilecoders.wicket.webjars.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.AssetLocator;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Callable that loads the recent version string of given webjars resource
 *
 * @author miha
 */
public class RecentVersionCallable implements Callable<String> {

    private static final Logger LOG = LoggerFactory.getLogger(WicketWebjars.class);
    private static final Pattern webjarsPattern = Pattern.compile("/webjars/([^/]*)/([^/]*)/(.*)");

    /**
     * Holder for reflection framework configuration builder
     */
    private static final class AssetLocatorConfigurationBuilder {
        private static ConfigurationBuilder instance = new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(StringUtils.join(AssetLocator.WEBJARS_PATH_PREFIX, "."), AssetLocator.class.getClassLoader()))
                .setScanners(new ResourcesScanner());
    }

    /**
     * creates a new future recent version collector
     *
     * @param partialPath the resource path
     * @return recent version as future
     */
    public static FutureTask<String> createFutureTask(final String partialPath) {
        return new FutureTask<String>(new RecentVersionCallable(partialPath));
    }

    private final String partialPath;

    /**
     * Construct.
     *
     * @param partialPath Path to webjars resource
     */
    private RecentVersionCallable(final String partialPath) {
        this.partialPath = partialPath;
    }

    @Override
    public String call() throws Exception {
        return collectRecentVersionFor(partialPath);
    }

    /**
     * collects recent version string of given webjars resource from classpath.
     *
     * @param partialPath The webjars resource path
     * @return recent version string
     */
    private static String collectRecentVersionFor(final String partialPath) {
        final Reflections reflections = new Reflections(AssetLocatorConfigurationBuilder.instance);
        final Matcher partialPathMatcher = webjarsPattern.matcher(partialPath);

        if (partialPathMatcher.find() && "current".equalsIgnoreCase(partialPathMatcher.group(2))) {
            final String fileName = partialPathMatcher.group(3);
            final List<String> versions = Lists.newArrayList();

            for (final Multimap<String, String> paths : reflections.getStore().getStoreMap().values()) {
                for (final String path : paths.values()) {
                    if (path.endsWith(fileName)) {
                        final Matcher matcher = webjarsPattern.matcher(path);

                        if (matcher.find()) {
                            versions.add(matcher.group(2));
                        }
                    }
                }
            }

            if (versions.size() == 1) {
                return versions.get(0);
            } else if (versions.size() > 1) {
                LOG.warn("more than one version of a dependency is not supported till now. webjars resource: {}; available versions: {}; using: {}",
                         new Object[] { fileName, versions, versions.get(0) });

                return versions.get(0);
            } else {
                LOG.debug("no version found for webjars resource: {}", partialPath);
            }
        } else {
            LOG.trace("given webjars resource isn't a dynamic versioned one: {}", partialPath);
        }

        return null;
    }
}