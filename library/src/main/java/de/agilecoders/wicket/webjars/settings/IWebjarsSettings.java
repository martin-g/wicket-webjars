package de.agilecoders.wicket.webjars.settings;

import java.util.regex.Pattern;

import org.apache.wicket.util.time.Duration;

import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;

/**
 * Settings interface for all webjars depended settings
 *
 * @author Michael Haitz <michael.haitz@agilecoders.de>
 */
public interface IWebjarsSettings {

    /**
     * @return {@link de.agilecoders.wicket.webjars.settings.ResourceStreamProvider} to use to load resources
     */
    ResourceStreamProvider resourceStreamProvider();

    /**
     * @return a set of {@link de.agilecoders.wicket.webjars.collectors.AssetPathCollector} instances to use to find
     * resources
     */
    AssetPathCollector[] assetPathCollectors();

    /**
     * @return the webjars package path (e.g. "META-INF.resources.webjars")
     */
    String webjarsPackage();

    /**
     * @return the path where all webjars are stored (e.g. "META-INF/resources/webjars")
     */
    String webjarsPath();

    /**
     * @return classloaders to use
     */
    ClassLoader[] classLoaders();

    /**
     * @return the pattern to filter accepted webjars resources
     */
    Pattern resourcePattern();

    /**
     * @return the full path pattern. The pattern must contain 3 groups: prefix, version, filename
     */
    Pattern webjarsPathPattern();

    /**
     * @return placeholder for recent version (e.g. current)
     */
    String recentVersionPlaceHolder();

    /**
     * @return timeout which is used when reading from cache (Future.get(timeout))
     */
    Duration readFromCacheTimeout();
    
    /**
     * @return true, if the resources for the webjars should be loaded from a CDN network
     */
    boolean useCdnResources();

    /**
     * @return true, if sourcemaps are active
     */
    boolean sourcemaps();
    
    /**
     * @return base URL of the webjars CDN
     */
    String cdnUrl();
    
}