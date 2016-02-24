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

/**
 * A collector that searches for assets in the classpath.
 */
public class ClasspathAssetPathCollector implements AssetPathCollector {

    private final String webjarsPath;

    public ClasspathAssetPathCollector(String webjarsPath) {
        this.webjarsPath = webjarsPath;
    }

    @Override
    public boolean accept(final URL url) {
        return true;
    }

    @Override
    public Collection<String> collect(final URL url, final Pattern filterExpr) {
        final Set<String> assetPaths = new HashSet<>();
        try {
            Enumeration<URL> webJarPathResources = Thread.currentThread().getContextClassLoader().getResources(webjarsPath);
            while (webJarPathResources.hasMoreElements()) {
                Set<String> paths = collectFromWebJarPath(webJarPathResources.nextElement());
                assetPaths.addAll(paths);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return assetPaths;
    }

    private Set<String> collectFromWebJarPath(URL webJarPathResource) throws IOException {
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
                    if (!isDirectory(innerJarEntryName)) {
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
