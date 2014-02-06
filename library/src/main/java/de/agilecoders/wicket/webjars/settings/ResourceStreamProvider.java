package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.util.ClassLoaderResourceStreamProvider;
import de.agilecoders.wicket.webjars.util.IResourceStreamProvider;
import de.agilecoders.wicket.webjars.util.UrlResourceStreamProvider;
import org.apache.wicket.util.string.Strings;

/**
 * A ResourceStreamProvider is responsible for creating resource streams. There are several
 * implementations that
 *
 * @author miha
 */
public enum ResourceStreamProvider {

    /**
     * The ClassLoader provider uses {@link ClassLoader#getResourceAsStream(String)} with a custom
     * {@link org.apache.wicket.util.resource.AbstractResourceStream} implementation.
     */
    ClassLoader {
        @Override
        public IResourceStreamProvider newInstance(ClassLoader... classLoaders) {
            return new ClassLoaderResourceStreamProvider(classLoaders);
        }
    },

    /**
     * The Url provider uses a {@link org.apache.wicket.core.util.resource.UrlResourceStream} to load
     * a resource. This provider can't be used on GAE, because it uses {@link java.net.URL#openConnection()}.
     */
    Url {
        @Override
        public IResourceStreamProvider newInstance(ClassLoader... classLoaders) {
            return new UrlResourceStreamProvider(classLoaders);
        }
    };

    /**
     * creates a new {@link de.agilecoders.wicket.webjars.util.IResourceStreamProvider} instance according to
     * this instance.
     *
     * @param classLoaders the class loaders to use to load/find resources
     * @return new {@link de.agilecoders.wicket.webjars.util.IResourceStreamProvider} instance
     */
    public abstract IResourceStreamProvider newInstance(ClassLoader... classLoaders);

    /**
     * @return best fitting {@link de.agilecoders.wicket.webjars.settings.ResourceStreamProvider}
     */
    public static ResourceStreamProvider bestFitting() {
        if (Strings.isEmpty(System.getProperty("com.google.appengine.runtime.environment"))) {
            return ClassLoader;
        } else {
            return Url;
        }
    }
}
