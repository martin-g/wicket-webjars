package de.agilecoders.wicket.webjars.collectors;

import org.jboss.vfs.VirtualFile;
import org.reflections.vfs.Vfs;
import org.webjars.AssetPathCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Adds support of vfs protocol to webjars.
 *
 * @author miha
 */
public class VfsJarAssetPathCollector extends AssetPathCollector.JarAssetPathCollector {
    private static final Pattern PATTERN = Pattern.compile("/([^/]*\\.jar)");

    /**
     * Construct.
     */
    public VfsJarAssetPathCollector() {
        super("vfs");

        addDefaultUrlTypes();
    }

    @Override
    protected JarFile newJarFile(URL url) {
        try {
            URLConnection conn = url.openConnection();
            VirtualFile vf = (VirtualFile) conn.getContent();
            File contentsFile = vf.getPhysicalFile();
            String c = contentsFile.getPath().replaceAll("\\", "/");

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

    private static AtomicBoolean added = new AtomicBoolean(false);

    private static void addDefaultUrlTypes() {
        if (!added.getAndSet(true)) {
            Vfs.addDefaultURLTypes(new VfsUrlType());
        }
    }
}