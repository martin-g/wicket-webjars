package de.agilecoders.wicket.webjars.util.osgi;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.reflections.vfs.Vfs.File;

/**
 * Turns the individual file {@link URL} objects into files that can be accesed
 * on disk
 * 
 * @author ben
 * 
 */
public class BundleFile implements File {

	private BundleDir bundleDir;
	private URL url;

	public BundleFile(BundleDir bundleDir, URL url) {
		this.bundleDir = bundleDir;
		this.url = url;
	}

	@Override
	public String getName() {
		return getRelativePath().substring(getRelativePath().lastIndexOf("/") + 1);
	}

	@Override
	public String getRelativePath() {
		return url.getFile().substring(bundleDir.getPath().length());
	}

	@Override
	public InputStream openInputStream() throws IOException {
		return url.openStream();
	}

}
