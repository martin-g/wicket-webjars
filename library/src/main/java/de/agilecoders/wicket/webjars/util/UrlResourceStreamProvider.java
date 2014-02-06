package de.agilecoders.wicket.webjars.util;

import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class UrlResourceStreamProvider implements IResourceStreamProvider {
    private static final Logger LOG = LoggerFactory.getLogger("wicket-webjars");

    private final ClasspathUrlStreamHandler urlHandler;

    public UrlResourceStreamProvider(ClassLoader... classLoaders) {
        this.urlHandler = new ClasspathUrlStreamHandler(classLoaders);
    }

    @Override
    public IResourceStream newResourceStream(String path) {
        try {
            return new UrlResourceStream(new URL(null, "classpath:" + path, urlHandler));
        } catch (MalformedURLException e) {
            LOG.warn("can't create URL to resource: {}", e.getMessage());
        }

        return null;
    }
}
