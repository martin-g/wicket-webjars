package de.agilecoders.wicket.webjars.util;

/**
 * full path to a webjars provider
 *
 * @author miha
 */
public interface IFullPathProvider {
    /**
     * Given a distinct path within the WebJar index passed in return the full path of the resource.
     *
     * @param partialPath the path to return e.g. "jquery.js" or "abc/someother.js". This must be a distinct path within the index passed in.
     * @return a fully qualified path to the resource.
     */
    String getFullPath(String partialPath);
}
