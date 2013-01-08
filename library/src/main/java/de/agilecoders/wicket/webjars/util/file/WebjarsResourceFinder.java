package de.agilecoders.wicket.webjars.util.file;

import de.agilecoders.wicket.webjars.request.resource.WebjarsCssResourceReference;
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
    private WebjarsResourceFinder() {
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
    public IResourceStream find(final Class<?> clazz, final String pathName) {


        if (WebjarsJavaScriptResourceReference.class.equals(clazz) || WebjarsCssResourceReference.class.equals(clazz)) {
            final int pos = pathName != null ? pathName.lastIndexOf("/webjars/") : -1;

            if (pos > -1) {
                try {
                    final String webjarsPath = AssetLocator.getFullPath(pathName.substring(pos));

                    LOG.debug("webjars path: {}", webjarsPath);

                    if (webjarsPath != null) {
                        for (WeakReference<ClassLoader> classLoader : classLoaders) {
                            final ClassLoader cl = classLoader.get();

                            if (cl != null) {
                                final URL url = cl.getResource(webjarsPath);

                                LOG.debug("webjars url: {} from: {}; used ClassLoader: {}", new Object[] { url, webjarsPath, classLoader.get() });

                                if (url != null) {
                                    return new UrlResourceStream(url);
                                }
                            }
                        }
                    }
                } catch (RuntimeException e) {
                    LOG.error("can't locate resource for: {}; {}", new Object[] {
                            pathName, e.getMessage(), e
                    });

                    return null;
                }

                LOG.debug("there is no webjars resource for: {}", pathName);
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "[webjars resource finder]";
    }

}
