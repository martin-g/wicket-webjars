package de.agilecoders.wicket.webjars.util.file;

import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import de.agilecoders.wicket.webjars.util.Helper;
import de.agilecoders.wicket.webjars.util.IFullPathProvider;
import de.agilecoders.wicket.webjars.util.IResourceStreamProvider;
import de.agilecoders.wicket.webjars.util.WebJarAssetLocator;
import de.agilecoders.wicket.webjars.util.WebjarsVersion;

import org.apache.wicket.util.file.IResourceFinder;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Knows how to find webjars resources.
 *
 * @author miha
 */
public class WebjarsResourceFinder implements IResourceFinder {
    private static final Logger LOG = LoggerFactory.getLogger("wicket-webjars");

    private final IFullPathProvider locator;
    private final IResourceStreamProvider resourceStreamProvider;
    private final IWebjarsSettings settings;
    private final int hashCode;

    /**
     * Construct.
     *
     * @param settings the webjars settings to use
     */
    public WebjarsResourceFinder(IWebjarsSettings settings) {
        this.settings = settings;
        this.locator = newFullPathProvider();
        this.resourceStreamProvider = settings.resourceStreamProvider().newInstance(settings.classLoaders());

        int _hashCode = locator != null ? locator.hashCode() : 0;
        this.hashCode = 31 * (_hashCode + settings.hashCode());
    }

    public void reindex() {
        if (locator instanceof WebJarAssetLocator) {
            WebJarAssetLocator webJarAssetLocator = (WebJarAssetLocator) locator;
            webJarAssetLocator.reindex();
        }
    }

    /**
     * @return new resource locator instance
     */
    protected IFullPathProvider newFullPathProvider() {
        return new WebJarAssetLocator(settings);
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

        if (clazz != null && IWebjarsResourceReference.class.isAssignableFrom(clazz)) {
            // pathname as extracted by wicket is a classpath resource path with no leading '/'
            // historically, webjars file locator works with /webjars/ prefixed path
            // prepend '/' and resolve version if needed
            String versionnedName = "/" + pathName;
            versionnedName = WebjarsVersion.useRecent(versionnedName); 
            final int pos = versionnedName != null ? versionnedName.lastIndexOf(Helper.PATH_PREFIX) : -1;

            if (pos > -1) {
                try {
                    final String webjarsPath = locator.getFullPath(versionnedName.substring(pos));

                    LOG.debug("webjars path: {}", webjarsPath);

                    stream = newResourceStream(webjarsPath);
                } catch (Exception e) {
                    LOG.debug("can't locate resource for: {} (actual name {}); {}", pathName, versionnedName, e.getMessage());
                }

                if (stream == null) {
                    LOG.debug("there is no webjars resource for: {} (actual name {})", pathName, versionnedName);
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
     * null if resource wasn't found
     */
    protected IResourceStream newResourceStream(final String webjarsPath) {
        return resourceStreamProvider.newResourceStream(webjarsPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WebjarsResourceFinder that = (WebjarsResourceFinder) o;

        if (locator != null ? !locator.equals(that.locator) : that.locator != null) {
            return false;
        }
        if (settings != null ? !settings.equals(that.settings) : that.settings != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
