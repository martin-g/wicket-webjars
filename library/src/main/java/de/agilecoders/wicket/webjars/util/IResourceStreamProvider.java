package de.agilecoders.wicket.webjars.util;

import org.apache.wicket.util.resource.IResourceStream;

/**
 * Creates a new {@link org.apache.wicket.util.resource.IResourceStream} that points to a given path.
 *
 * @author miha
 */
public interface IResourceStreamProvider {

    /**
     * Creates a new {@link org.apache.wicket.util.resource.IResourceStream} that points to a given path.
     *
     * @param path the path to load
     * @return new {@link org.apache.wicket.util.resource.IResourceStream} instance
     */
    IResourceStream newResourceStream(final String path);
}
