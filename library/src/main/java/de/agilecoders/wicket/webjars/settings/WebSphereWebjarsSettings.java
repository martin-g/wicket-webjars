package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.FileAssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.VfsAssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.WebSphereClasspathAssetPathCollector;

/**
 * {@link IWebjarsSettings} which should be used when deploying on IBM WebSphere
 *
 * <strong>Make sure to add dependency on edu.emory.mathcs.util:emory-util-classloader to the classpath!</strong>
 *
 * @see WebSphereClasspathAssetPathCollector
 */
public class WebSphereWebjarsSettings extends WebjarsSettings{

    public WebSphereWebjarsSettings() {
        super();

        // WebSphere needs a trailing slash to list resources with ClassLoader#getResources(String)
        webjarsPath(webjarsPath() + "/");

        //Adding custom AssetPathCollector
        AssetPathCollector[] webSphereAssetPathCollectors = new AssetPathCollector[] {
                new WebSphereClasspathAssetPathCollector(),
                new VfsAssetPathCollector(),
                new FileAssetPathCollector(webjarsPath())
        };

        assetPathCollectors(webSphereAssetPathCollectors);
    }
}
