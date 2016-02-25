package de.agilecoders.wicket.webjars.collectors;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;

/**
 * A collector that searches for assets in the classpath, only in
 * {@link IWebjarsSettings#webjarsPath()}, usually in <em>META-INF/resources/webjars/**</em>.
 */
public class ClasspathAssetPathCollector implements AssetPathCollector {

    @Override
    public boolean accept(final URL url) {
        return true;
    }

    @Override
    public Collection<String> collect(final URL url, final Pattern filterExpr) {
        final Set<String> assetPaths = new HashSet<>();
        try {
            Set<String> paths = collectFromWebJarPath(url, filterExpr);
            assetPaths.addAll(paths);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return assetPaths;
    }

    private Set<String> collectFromWebJarPath(URL webJarPathResource, final Pattern filterExpr) throws IOException {
        final Set<String> assetPaths = new HashSet<>();

        URLConnection urlConnection = webJarPathResource.openConnection();
        if (urlConnection instanceof JarURLConnection) {
            JarURLConnection urlcon = (JarURLConnection) urlConnection;
            JarFile jar = null;
            try {
                jar = urlcon.getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    String innerJarEntryName = entries.nextElement().getName();
                    if (!isDirectory(innerJarEntryName) && filterExpr.matcher(innerJarEntryName).matches()) {
                        assetPaths.add(innerJarEntryName);
                    }
                }
            } finally {
                if (jar != null) {
                    jar.close();
                }
            }
        }
        return assetPaths;
    }

    private boolean isDirectory(String innerJarEntryName) {
        return innerJarEntryName.endsWith("/");
    }
}
