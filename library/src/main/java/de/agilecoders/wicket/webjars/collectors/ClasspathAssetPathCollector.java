package de.agilecoders.wicket.webjars.collectors;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The 
 * 
 * @author Marc Giffing
 *
 */
public class ClasspathAssetPathCollector {


	public Collection<String> collect(String webjarsPath) {
		final Set<String> assetPaths = new HashSet<>();
		try {
			Enumeration<URL> webJarPathResources = Thread.currentThread().getContextClassLoader().getResources(webjarsPath);
			while (webJarPathResources.hasMoreElements()) {
				assetPaths.addAll(collectFromWebJarPath(webJarPathResources.nextElement()));
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return assetPaths;
	}

	private Set<String> collectFromWebJarPath(URL webJarPathResource)
			throws IOException {
		final Set<String> assetPaths = new HashSet<>();
		
		JarURLConnection urlcon = (JarURLConnection) (webJarPathResource.openConnection());
		try (JarFile jar = urlcon.getJarFile();) {
		    Enumeration<JarEntry> entries = jar.entries();
		    while (entries.hasMoreElements()) {
		        String innerJarEntryName = entries.nextElement().getName();
		        	if (isNotADirectory(innerJarEntryName)) {
		        		assetPaths.add(innerJarEntryName);
		        	}
		    }
		}
		return assetPaths;
	}
	
	//maybe there is a better way to check if its a directory
	private boolean isNotADirectory(String innerJarEntryName) {
		return !isDirectory(innerJarEntryName);
	}

	private boolean isDirectory(String innerJarEntryName) {
		return innerJarEntryName.endsWith("/");
	}
	
}
