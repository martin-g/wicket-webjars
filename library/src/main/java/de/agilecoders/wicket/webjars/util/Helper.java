package de.agilecoders.wicket.webjars.util;

import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.string.Strings;

/**
 * some helper methods
 *
 * @author miha
 */
public final class Helper {

    public static final String PATH_PREFIX = "/webjars/";

    /**
     * prepends the webjars path if missing
     *
     * @param path the file name to check
     * @return file name that starts with "/webjars/"
     */
    public static String prependWebjarsPathIfMissing(final String path) {
        final String cleanedName = prependLeadingSlash(Args.notEmpty(path, "path"));

        if (!path.contains(PATH_PREFIX)) {
            return "/webjars" + cleanedName;
        }

        return path;
    }

    /**
     * prepends a leading slash if there is none.
     *
     * @param path the path
     * @return path with leading slash
     * @deprecated Use {@link #prependLeadingSlash(String)}
     */
    @Deprecated
    public static String appendLeadingSlash(final String path) {
        return prependLeadingSlash(path);
    }

    /**
     * prepends a leading slash if there is none.
     *
     * @param path the path
     * @return path with leading slash
     */
    public static String prependLeadingSlash(final String path) {
        return path.charAt(0) == '/' ? path : '/' + path;
    }

    /**
     * Removes the leading slash if there is one.
     *
     * @param path the path
     * @return path without leading slash
     */
    public static String removeLeadingSlash(final String path) {
        return path.charAt(0) == '/' ? path.substring(1) : path;
    }

    /**
     * Make paths like aa/bb/cc = cc/bb/aa.
     *
     * @param assetPath the path to revert
     * @return reverted path
     */
    public static String reversePath(String assetPath) {
        final String[] assetPathComponents = Strings.split(assetPath, '/');
        final StringBuilder reversedAssetPath = new StringBuilder();
        for (int i = assetPathComponents.length - 1; i >= 0; --i) {
            if (reversedAssetPath.length() > 0) {
                reversedAssetPath.append('/');
            }
            reversedAssetPath.append(assetPathComponents[i]);
        }
        return reversedAssetPath.toString();
    }

}
