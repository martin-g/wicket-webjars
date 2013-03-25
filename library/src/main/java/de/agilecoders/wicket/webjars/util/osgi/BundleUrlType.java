package de.agilecoders.wicket.webjars.util.osgi;

import java.net.URL;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.reflections.vfs.Vfs.Dir;
import org.reflections.vfs.Vfs.UrlType;
import org.webjars.AssetLocator;

/**
 * Manages a bundle {@link URL} type
 * 
 * @author ben
 * 
 */
public class BundleUrlType implements UrlType {

	//BD - Note that this changes to bundle in R4.3
	private static final String BUNDLE_PROTOCOL = "bundleresource";

	@Override
	public boolean matches(URL url) throws Exception {
		return BUNDLE_PROTOCOL.equals(url.getProtocol());
	}

	@Override
	public Dir createDir(URL url) throws Exception {

		// BD - Use FrameworkUtil to fetch the bundle that this class exists in
		Bundle bundle = FrameworkUtil.getBundle(AssetLocator.class);
		Dir dir = null;

		// Assuming it's not null, create a BundleDir
		if (bundle != null) {
			dir = new BundleDir(bundle, url);
		}

		return dir;
	}

}
