package org.webjars;

import org.jboss.vfs.VirtualFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO miha: document class purpose
 *
 * @author miha
 */
public interface AssetPathCollector {

    boolean accept(URL url);

    Collection<String> collect(URL url, Pattern filterExpr);

    public static abstract class ProtocolAwareAssetPathCollector implements AssetPathCollector {

        private final String protocol;

        protected ProtocolAwareAssetPathCollector(final String protocol) {
            this.protocol = protocol;
        }

        @Override
        public boolean accept(URL url) {
            return url != null && protocol.equalsIgnoreCase(url.getProtocol());
        }
    }

    public static class FileAssetPathCollector extends ProtocolAwareAssetPathCollector {
        private static final int MAX_DIRECTORY_DEPTH = 5;

        private final String pathPrefix;

        public FileAssetPathCollector(final String pathPrefix) {
            super("file");

            this.pathPrefix = pathPrefix;
        }

        @Override
        public Collection<String> collect(URL url, Pattern filterExpr) {
            final File file;
            try {
                file = new File(url.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            return listFiles(file, filterExpr);
        }

        /**
         * Recursively search all directories for relative file paths matching `filterExpr`.
         *
         * @param file
         * @param filterExpr
         * @return
         */
        private Set<String> listFiles(final File file, final Pattern filterExpr) {
            final Set<String> aggregatedChildren = new HashSet<String>();
            aggregateChildren(file, file, aggregatedChildren, filterExpr, 0);
            return aggregatedChildren;
        }

        private void aggregateChildren(final File rootDirectory, final File file, final Set<String> aggregatedChildren, final Pattern filterExpr, final int level) {
            if (file != null && file.isDirectory()) {
                if (level > MAX_DIRECTORY_DEPTH) {
                    throw new IllegalStateException("Got deeper than " + MAX_DIRECTORY_DEPTH + " levels while searching " + rootDirectory);
                }

                File[] files = file.listFiles();

                if (files != null) {
                    for (final File child : files) {
                        aggregateChildren(rootDirectory, child, aggregatedChildren, filterExpr, level + 1);
                    }
                }
            } else if (file != null) {
                aggregateFile(file, aggregatedChildren, filterExpr);
            }
        }

        private void aggregateFile(final File file, final Set<String> aggregatedChildren, final Pattern filterExpr) {
            final String path = file.getPath();
            final String relativePath = path.substring(path.indexOf(pathPrefix));
            if (filterExpr.matcher(relativePath).matches()) {
                aggregatedChildren.add(relativePath);
            }
        }
    }

    public static class JarAssetPathCollector extends ProtocolAwareAssetPathCollector {

        public JarAssetPathCollector() {
            super("jar");
        }

        protected JarAssetPathCollector(final String protocol) {
            super(protocol);
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
                throw new RuntimeException(e);
            }
        }
    }

    public static class VfsJarAssetPathCollector extends JarAssetPathCollector {
        private static final Pattern PATTERN = Pattern.compile("/([^/]*\\.jar)");

        public VfsJarAssetPathCollector() {
            super("vfs");
        }

        @Override
        protected JarFile newJarFile(URL url) {
            try {
                URLConnection conn = url.openConnection();
                VirtualFile vf = (VirtualFile) conn.getContent();
                File contentsFile = vf.getPhysicalFile();
                String c = contentsFile.getPath();

                final String jarName = toJarName(url);
                final String pathToJar = c.substring(0, c.indexOf("/contents/"));

                return new JarFile(new File(pathToJar, jarName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static String toJarName(URL url) throws FileNotFoundException {
            final String path = url.getPath();

            Matcher m = PATTERN.matcher(path);
            if (m.find()) {
                return m.group(1);
            }

            throw new FileNotFoundException(url.getPath());
        }
    }
}
