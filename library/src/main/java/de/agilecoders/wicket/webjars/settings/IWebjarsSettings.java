package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;
import org.apache.wicket.util.time.Duration;

import java.util.regex.Pattern;

/**
 * Settings interface for all webjars depended settings
 *
 * @author miha
 */
public interface IWebjarsSettings {
	
    /**
     * The url of the WebJars CDN.
     */
    String WEB_JAR_CDN = "//www.jsdelivr.com/";

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
    
}