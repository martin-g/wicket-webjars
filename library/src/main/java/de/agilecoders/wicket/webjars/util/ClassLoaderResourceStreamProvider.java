package de.agilecoders.wicket.webjars.util;

import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IFixedLocationResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public class ClassLoaderResourceStreamProvider implements IResourceStreamProvider {
    private static final Logger LOG = LoggerFactory.getLogger("wicket-webjars");

    private final ClassLoader[] classLoaders;

    public ClassLoaderResourceStreamProvider(ClassLoader... classLoaders) {
        this.classLoaders = classLoaders;
    }

    @Override
    public IResourceStream newResourceStream(String path) {
        for (ClassLoader loader : classLoaders) {
            try {
                InputStream resource = loader.getResourceAsStream(path);

                if (resource != null) {
                    return new InputStreamResourceStream(path, resource);
                }
            } catch (RuntimeException e) {
                LOG.warn("can't load resource: {}", e.getMessage());
            }
        }

        return null;
    }

    private static final class InputStreamResourceStream extends AbstractResourceStream implements
            IFixedLocationResourceStream {

        private final String path;
        private final InputStream inputStream;

        private InputStreamResourceStream(String path, InputStream inputStream) {
            this.path = path;
            this.inputStream = inputStream;
        }

        @Override
        public String locationAsString() {
            return path;
        }

        @Override
        public InputStream getInputStream() throws ResourceStreamNotFoundException {
            return inputStream;
        }

        @Override
        public void close() throws IOException {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
