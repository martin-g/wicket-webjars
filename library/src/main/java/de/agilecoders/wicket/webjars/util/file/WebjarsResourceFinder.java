package de.agilecoders.wicket.webjars.util.file;

import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.AssetLocator;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Knows how to find webjars resources.
 *
 * @author miha
 */
public class WebjarsResourceFinder implements IResourceFinder {
    private static final Logger LOG = LoggerFactory.getLogger(WebjarsResourceFinder.class);

    private final Set<WeakReference<ClassLoader>> classLoaders = new HashSet<WeakReference<ClassLoader>>();

    /**
     * Holder for webjars resource finder
     */
    private static final class Holder {
        private static final WebjarsResourceFinder instance = new WebjarsResourceFinder();
    }

    /**
     * @return webjars resource finder
     */
    public static WebjarsResourceFinder instance() {
        return Holder.instance;
    }

    /**
     * Construct.
     */
    protected WebjarsResourceFinder() {
        addClassLoader(Thread.currentThread().getContextClassLoader(),
                       AssetLocator.class.getClassLoader(),
                       getClass().getClassLoader());
    }

    /**
     * adds a {@link ClassLoader} to the set of {@link ClassLoader} to use for resource lookup
     *
     * @param classLoaders array of {@link ClassLoader}
     */
    protected final void addClassLoader(ClassLoader... classLoaders) {
        Args.notNull(classLoaders, "classLoaders");

        for (ClassLoader classLoader : classLoaders) {
            this.classLoaders.add(new WeakReference<ClassLoader>(classLoader));
        }
    }

    /**
     * Looks for a given path name along the webjars root path
     *
     * @param clazz    The class requesting the resource stream
     * @param pathName The filename with possible path
     * @return The resource stream
     */
    @Override
    public IResourceStream find(final Class<?> clazz, final String pathName) {
        if (IWebjarsResourceReference.class.isAssignableFrom(clazz)) {
            final int pos = pathName != null ? pathName.lastIndexOf("/webjars/") : -1;

            if (pos > -1) {
                try {
                    final String webjarsPath = AssetLocator.getFullPath(pathName.substring(pos));

                    LOG.debug("webjars path: {}", webjarsPath);

                    if (webjarsPath != null) {
                        for (WeakReference<ClassLoader> weakClassLoader : classLoaders) {
                            final ClassLoader classLoader = weakClassLoader.get();

                            if (classLoader != null) {
                                final IResourceStream stream = newResourceStream(classLoader, webjarsPath);

                                if (stream != null) {
                                    return stream;
                                }
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    LOG.error("can't locate resource for: {}; {}", new Object[] {
                            pathName, e.getMessage(), e
                    });

                    throw new WicketRuntimeException(e);
                }

                LOG.debug("there is no webjars resource for: {}", pathName);
            }
        }

        return null;
    }

    /**
     * creates a new {@link IResourceStream} for given resource path with should be loaded by given
     * class loader.
     *
     * @param classLoader The class loader to use for resource loading
     * @param webjarsPath The resource to load
     * @return new {@link IResourceStream} instance that represents the content of given resource path or
     *         null if resource wasn't found
     */
    protected IResourceStream newResourceStream(final ClassLoader classLoader, final String webjarsPath) {
        final URL url = classLoader.getResource(webjarsPath);

        LOG.debug("webjars url: {} from: {}; used ClassLoader: {}", new Object[] { url, webjarsPath, classLoader });

        if (url != null) {
            return new UrlResourceStream(url);
        }

        return null;
    }

    @Override
    public String toString() {
        return "[webjars resource finder]";
    }

}
