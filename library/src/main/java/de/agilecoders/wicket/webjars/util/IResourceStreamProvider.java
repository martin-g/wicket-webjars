package de.agilecoders.wicket.webjars.util;

import org.apache.wicket.util.resource.IResourceStream;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public interface IResourceStreamProvider {

    IResourceStream newResourceStream(final String path);
}
