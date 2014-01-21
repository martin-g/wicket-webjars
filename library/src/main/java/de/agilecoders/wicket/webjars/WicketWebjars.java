package de.agilecoders.wicket.webjars;

import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;
import de.agilecoders.wicket.webjars.util.file.WebjarsResourceFinder;
import org.apache.wicket.Application;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.lang.Args;

import java.util.List;

/**
 * Helper class for webjars resources
 *
 * @author miha
 */
public final class WicketWebjars {
    private static final String PATH_SPLITTER = "/";

    /**
     * registers an additional asset collector
     *
     * @param collectorArr the collectors to register
     */
    public static void registerCollector(AssetPathCollector... collectorArr) {
        WebJarAssetLocator.registerCollector(collectorArr);
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
        return path.charAt(0) == '/' ? path : PATH_SPLITTER + path;
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
    private WicketWebjars() {
        throw new UnsupportedOperationException();
    }
}
