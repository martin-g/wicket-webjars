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
import java.util.regex.Pattern;

public class ClasspathAssetPathCollector {


	public Collection<String> collect(Pattern filterExpr) {
		final Set<String> assetPaths = new HashSet<>();
		try {
			Enumeration<URL> systemResources = Thread.currentThread().getContextClassLoader().getResources("META-INF/resources/webjars/");
			while (systemResources.hasMoreElements()) {
				URL nextElement = systemResources.nextElement();
				JarURLConnection urlcon = (JarURLConnection) (nextElement.openConnection());
		        try (JarFile jar = urlcon.getJarFile();) {
		            Enumeration<JarEntry> entries = jar.entries();
		            while (entries.hasMoreElements()) {
		                String innerJarEntryName = entries.nextElement().getName();
		                if(filterExpr.matcher(innerJarEntryName).matches()){
		                	if (!innerJarEntryName.endsWith("/") && filterExpr.matcher(innerJarEntryName).matches()) {
		                		assetPaths.add(innerJarEntryName);
		                	}
		            	}
		            }
		        }
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		return assetPaths;
	}
	
}
