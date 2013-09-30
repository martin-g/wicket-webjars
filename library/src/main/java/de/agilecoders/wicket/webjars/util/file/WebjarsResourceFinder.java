package de.agilecoders.wicket.webjars.util.file;

import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import de.agilecoders.wicket.webjars.util.ClasspathUrlStreamHandler;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webjars.WebJarAssetLocator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Knows how to find webjars resources.
 *
 * @author miha
 */
public class WebjarsResourceFinder implements IResourceFinder {
    private static final Logger LOG = LoggerFactory.getLogger(WebjarsResourceFinder.class);

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

    private final WebJarAssetLocator locator;
    private final ClasspathUrlStreamHandler urlHandler;

    /**
     * Construct.
     */
    protected WebjarsResourceFinder() {
        ClassLoader[] classLoaders = new ClassLoader[] {
                Thread.currentThread().getContextClassLoader(),
                WebJarAssetLocator.class.getClassLoader(),
                getClass().getClassLoader()
        };

        locator = newLocator(classLoaders);
        urlHandler = new ClasspathUrlStreamHandler(classLoaders);
    }

    /**
     * @param classLoaders the classloaders to use to load resources
     * @return new resource locator instance
     */
    protected WebJarAssetLocator newLocator(ClassLoader[] classLoaders) {
        return new WebJarAssetLocator(
                WebJarAssetLocator.getFullPathIndex(Pattern.compile(".*"), classLoaders)
        );
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
        IResourceStream stream = null;

        if (IWebjarsResourceReference.class.isAssignableFrom(clazz)) {
            final int pos = pathName != null ? pathName.lastIndexOf("/webjars/") : -1;

            if (pos > -1) {
                try {
                    final String webjarsPath = locator.getFullPath(pathName.substring(pos));

                    LOG.debug("webjars path: {}", webjarsPath);

                    stream = newResourceStream(webjarsPath);
                } catch (RuntimeException e) {
                    LOG.error("can't locate resource for: {}; {}", pathName, e.getMessage(), e);

                    throw new WicketRuntimeException(e);
                }

                if (stream != null) {
                    LOG.debug("there is no webjars resource for: {}", pathName);
                }
            }

        }

        return stream;
    }

    /**
     * creates a new {@link IResourceStream} for given resource path with should be loaded by given
     * class loader.
     *
     * @param webjarsPath The resource to load
     * @return new {@link IResourceStream} instance that represents the content of given resource path or
     *         null if resource wasn't found
     */
    protected IResourceStream newResourceStream(final String webjarsPath) {
        try {
            return new UrlResourceStream(new URL(null, "classpath:" + webjarsPath, urlHandler));
        } catch (MalformedURLException e) {
            LOG.warn("can't create URL to resource: {}", e.getMessage());
        }

        return null;
    }

    @Override
    public String toString() {
        return "[webjars resource finder]";
    }

}
