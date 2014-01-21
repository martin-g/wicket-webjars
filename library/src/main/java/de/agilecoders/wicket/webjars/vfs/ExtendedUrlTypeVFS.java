package de.agilecoders.wicket.webjars.vfs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.vfs.VirtualFile;
import org.reflections.vfs.UrlTypeVFS;
import org.reflections.vfs.Vfs.Dir;

/**
 * handles vfs urls.
 * 
 * @author miha
 */
public class ExtendedUrlTypeVFS extends UrlTypeVFS {

	final static boolean jbossAS;

	final String VFS = "vfs";

	static {
		boolean jbossFound;
		try {
			Class.forName("org.jboss.vfs.VirtualFile");
			jbossFound = true;
		} catch (ClassNotFoundException e) {
			jbossFound = false;
		}

		jbossAS = jbossFound;
	}

	@Override
	public boolean matches(URL url) {
		return super.matches(url) || VFS.equals(url.getProtocol());
	}

	@Override
	public Dir createDir(URL url) {
		if (jbossAS && VFS.equals(url.getProtocol())) {
			try {
				if (url.getContent() instanceof VirtualFile) {
					return new VfsDir(url);
				}
			} catch (IOException e) {
				throw new RuntimeException("error reading from VFS", e);
			}
		}
		return super.createDir(url);
	}

	@Override
	public URL adaptURL(URL url) throws MalformedURLException {
		if (VFS.equals(url.getProtocol())) {
			return new URL(url.toString().replace(VFS, "file"));
		} else {
			return super.adaptURL(url);
		}
	}
}
