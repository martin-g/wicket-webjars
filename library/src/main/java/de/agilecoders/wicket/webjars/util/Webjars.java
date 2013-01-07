package de.agilecoders.wicket.webjars.util;

import org.apache.wicket.util.lang.Args;

/**
 * Helper class for webjars resources
 *
 * @author miha
 */
public final class Webjars {

    private static final String PATH_SPLITTER = "/";

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
     * private constructor.
     */
    private Webjars() {
        throw new UnsupportedOperationException();
    }
}
