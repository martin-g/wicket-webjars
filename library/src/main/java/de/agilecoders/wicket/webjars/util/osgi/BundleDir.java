package de.agilecoders.wicket.webjars.util.osgi;

import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import org.osgi.framework.Bundle;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.Vfs.Dir;
import org.reflections.vfs.Vfs.File;

import com.google.common.collect.AbstractIterator;

/**
 * Let's us find all of the file entries both provided by the bundle its self
 * and any attached fragments
 * 
 * @author ben
 * 
 */
public class BundleDir implements Dir {

	private Bundle bundle;
	private URL url;

	public BundleDir(Bundle bundle, URL url) {
		this.url = url;
		this.bundle = bundle;
	}

	@Override
	public String getPath() {
		return url.getPath();
	}

	@Override
	public Iterable<File> getFiles() {
		return new Iterable<Vfs.File>() {
			public Iterator<Vfs.File> iterator() {
				return new AbstractIterator<Vfs.File>() {

					//BD - Get all of the entries in this bundle
					final Enumeration<URL> entries = bundle.findEntries("", "*", true);

					protected Vfs.File computeNext() {
						while (entries.hasMoreElements()) {
							URL entry = entries.nextElement();

							//BD - Exclude dirs, as with ZipDir
							if (!entry.getFile().endsWith("/")) {
								return new BundleFile(BundleDir.this, entry);
							}
						}

						return endOfData();
					}
				};
			}
		};
	}

	@Override
	public void close() {

	}

}
