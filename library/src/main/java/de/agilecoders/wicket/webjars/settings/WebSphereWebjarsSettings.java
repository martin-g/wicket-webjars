package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.FileAssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.VfsAssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.WebSphereClasspathAssetPathCollector;

public class WebSphereWebjarsSettings extends WebjarsSettings{

	public WebSphereWebjarsSettings() {
		super();
	
		//Adding trailing slash
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
