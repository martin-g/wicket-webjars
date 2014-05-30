package de.agilecoders.wicket.webjars.collectors;

import de.agilecoders.wicket.webjars.util.WebJarAssetLocator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class JarAssetPathCollector extends ProtocolAwareAssetPathCollector {

    /**
     * Construct accepting the jar protocol.
     */
    public JarAssetPathCollector() {
        super("jar");
    }

    /**
     * Construct.
     *
     * @param protocols the protocols to accept
     */
    protected JarAssetPathCollector(final String... protocols) {
        super(protocols);
    }

    @Override
    public Collection<String> collect(URL url, Pattern filterExpr) {
        final JarFile jarFile = newJarFile(url);
        final Set<String> assetPaths = new HashSet<String>();

        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String assetPathCandidate = entry.getName();

            if (!entry.isDirectory() && filterExpr.matcher(assetPathCandidate).matches()) {
                assetPaths.add(assetPathCandidate);
            }
        }

        return assetPaths;
    }

    protected JarFile newJarFile(final URL url) {
        try {
            final String path = url.getPath();
            final File file = new File(URI.create(path.substring(0, path.indexOf("!"))));

            return new JarFile(file);
        } catch (IOException e) {
            throw new WebJarAssetLocator.ResourceException(url.toString(), e.getMessage());
        }
    }
}