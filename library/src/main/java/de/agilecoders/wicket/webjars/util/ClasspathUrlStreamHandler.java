package de.agilecoders.wicket.webjars.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * A {@link URLStreamHandler} that handles resources on the classpath.
 *
 * @author miha
 */
public class ClasspathUrlStreamHandler extends URLStreamHandler {

    /**
     * The classloaders to find resources from.
     */
    private final ClassLoader[] classLoaders;

    /**
     * Construct.
     *
     * @param classLoaders The classloaders to find resources from.
     */
    public ClasspathUrlStreamHandler(ClassLoader... classLoaders) {
        this.classLoaders = classLoaders;
    }

    @Override
    protected URLConnection openConnection(URL url) throws IOException {
        for (ClassLoader classLoader : classLoaders) {
            final URL resourceUrl = classLoader.getResource(url.getPath());

            if (resourceUrl != null) {
                return resourceUrl.openConnection();
            }
        }

        throw new IOException("can't find resource with url: " + url);
    }
}