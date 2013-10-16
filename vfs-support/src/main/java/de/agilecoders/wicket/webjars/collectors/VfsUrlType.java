package de.agilecoders.wicket.webjars.collectors;

import org.jboss.vfs.VirtualFile;
import org.reflections.ReflectionsException;
import org.reflections.vfs.SystemDir;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.ZipDir;

import java.io.IOException;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * handles vfs urls.
 *
 * @author miha
 */
public class VfsUrlType implements Vfs.UrlType {

    @Override
    public boolean matches(URL url) {
        return url.getProtocol().equals("vfs");
    }

    @Override
    public Vfs.Dir createDir(URL url) {
        VirtualFile content;
        try {
            content = (VirtualFile) url.openConnection().getContent();
        } catch (Throwable e) {
            throw new ReflectionsException("could not open url connection as VirtualFile [" + url + "]", e);
        }

        Vfs.Dir dir = null;
        try {
            dir = createDir(new java.io.File(content.getPhysicalFile().getParentFile(), content.getName()));
        } catch (IOException e) { /*continue*/ }
        if (dir == null) {
            try {
                dir = createDir(content.getPhysicalFile());
            } catch (IOException e) { /*continue*/ }
        }
        return dir;
    }

    private Vfs.Dir createDir(java.io.File file) {
        try {
            return file.exists() && file.canRead() ? file.isDirectory() ? new SystemDir(file) : new ZipDir(new JarFile(file)) : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
