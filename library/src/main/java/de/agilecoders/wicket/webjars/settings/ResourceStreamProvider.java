package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.util.ClassLoaderResourceStreamProvider;
import de.agilecoders.wicket.webjars.util.IResourceStreamProvider;
import de.agilecoders.wicket.webjars.util.UrlResourceStreamProvider;
import org.apache.wicket.util.string.Strings;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public enum ResourceStreamProvider {

    ClassLoader {
        @Override
        public IResourceStreamProvider newInstance(ClassLoader... classLoaders) {
            return new ClassLoaderResourceStreamProvider(classLoaders);
        }
    },

    Url {
        @Override
        public IResourceStreamProvider newInstance(ClassLoader... classLoaders) {
            return new UrlResourceStreamProvider(classLoaders);
        }
    };

    public abstract IResourceStreamProvider newInstance(ClassLoader... classLoaders);

    public static ResourceStreamProvider bestFitting() {
        if (Strings.isEmpty(System.getProperty("com.google.appengine.runtime.environment"))) {
            return ClassLoader;
        } else {
            return Url;
        }
    }
}
