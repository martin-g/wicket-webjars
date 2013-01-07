package de.agilecoders.wicket.webjars.util.file;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.util.file.IResourceFinder;
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
     * Construct.
     */
    public WebjarsResourceFinder() {
        classLoaders.add(new WeakReference<ClassLoader>(WebjarsJavaScriptResourceReference.class.getClassLoader()));
        classLoaders.add(new WeakReference<ClassLoader>(Thread.currentThread().getContextClassLoader()));
        classLoaders.add(new WeakReference<ClassLoader>(AssetLocator.class.getClassLoader()));
        classLoaders.add(new WeakReference<ClassLoader>(getClass().getClassLoader()));
    }

    /**
     * Looks for a given path name along the webjars root path
     *
     * @param clazz    The class requesting the resource stream
     * @param pathName The filename with possible path
     * @return The resource stream
     */
    @Override
    public IResourceStream find(Class<?> clazz, String pathName) {
        final int pos = pathName != null ? pathName.lastIndexOf("/webjars/") : -1;

        if (WebjarsJavaScriptResourceReference.class.equals(clazz) && pos > -1) {
            try {
                final String webjarsPath = AssetLocator.getWebJarPath(pathName.substring(pos));

                LOG.debug("webjars path: {}", webjarsPath);

                if (webjarsPath != null) {
                    final String resourcePath = "META-INF/resources/" + webjarsPath;

                    for (WeakReference<ClassLoader> classLoader : classLoaders) {
                        final URL url = classLoader.get().getResource(resourcePath);

                        LOG.debug("webjars url: {} from: {}; used ClassLoader: {}", new Object[] { url, resourcePath, classLoader.get() });

                        if (url != null) {
                            return new UrlResourceStream(url);
                        }
                    }
                }
            } catch (RuntimeException e) {
                LOG.error("can't locate resource for: {}; {}", new Object[] {
                        pathName, e.getMessage(), e
                });
                return null;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "[webjars resource finder]";
    }

}
