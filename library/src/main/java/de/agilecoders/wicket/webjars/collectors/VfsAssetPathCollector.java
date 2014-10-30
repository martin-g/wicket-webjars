package de.agilecoders.wicket.webjars.collectors;

import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

/**
 * An {@link AssetPathCollector} that collects all file entries in JBoss virtual file system
 */
public class VfsAssetPathCollector extends ProtocolAwareAssetPathCollector {

    /**
     * Construct accepting the jar protocol.
     */
    public VfsAssetPathCollector() {
        super("vfs");
    }

    @Override
    public Collection<String> collect(URL url, Pattern filterExpr) {
        final Set<String> assetPaths = new HashSet<String>();
        try {
            URLConnection connection = url.openConnection();
            JarInputStream inputStream = (JarInputStream) connection.getInputStream();

            JarEntry entry;
            while ((entry = inputStream.getNextJarEntry()) != null) {
                String entryName = entry.getName();
                if (!entry.isDirectory() && filterExpr.matcher(entryName).matches()) {
                    assetPaths.add("META-INF/resources/webjars/" + entryName);
                }

            }
        } catch (Exception x) {
            throw new RuntimeException("Cannot collect the file entries in url: " + url, x);
        }

        return assetPaths;
    }
}
