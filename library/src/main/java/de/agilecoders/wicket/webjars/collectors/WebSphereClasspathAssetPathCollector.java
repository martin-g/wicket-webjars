package de.agilecoders.wicket.webjars.collectors;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import edu.emory.mathcs.util.classloader.jar.JarProxy;

/**
 * A collector that searches for assets in the classpath, only in
 * {@link IWebjarsSettings#webjarsPath()}, usually in <em>META-INF/resources/webjars/**</em>.
 *
 * <strong>Make sure to add dependency on edu.emory.mathcs.util:emory-util-classloader to the classpath!</strong>
 *
 * @see de.agilecoders.wicket.webjars.settings.WebSphereWebjarsSettings
 */
public class WebSphereClasspathAssetPathCollector implements AssetPathCollector {

    @Override
    public boolean accept(final URL url) {
        return true;
    }

    @Override
    public Collection<String> collect(final URL url, final Pattern filterExpr) {
        final Set<String> assetPaths = new HashSet<String>();
        try {
            Set<String> paths = collectFromWebJarPath(url, filterExpr);
            assetPaths.addAll(paths);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return assetPaths;
    }

    private Set<String> collectFromWebJarPath(URL webJarPathResource, final Pattern filterExpr) throws IOException {
        final Set<String> assetPaths = new HashSet<String>();

        edu.emory.mathcs.util.classloader.jar.JarURLConnection urlConnection =
                new edu.emory.mathcs.util.classloader.jar.JarURLConnection(webJarPathResource, new JarProxy());
        
        JarFile jar = null;
        try {
            jar = urlConnection.getJarFile();
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
        return assetPaths;
    }

    private boolean isDirectory(String innerJarEntryName) {
        return innerJarEntryName.endsWith("/");
    }
}
