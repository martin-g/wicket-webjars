package de.agilecoders.wicket.webjars.vfs;

import com.google.common.collect.AbstractIterator;
import org.jboss.vfs.VirtualFile;
import org.reflections.vfs.Vfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link Vfs.Dir} to support JBoss VFS for proper classpath scanning on JBoss AS.
 *
 * @author johnjcool
 */
public class VfsDir implements Vfs.Dir {

    private VirtualFile virtualFile;

    public VfsDir(URL url) {
        try {
            Object content = url.getContent();
            if (content instanceof VirtualFile) {
                virtualFile = (VirtualFile) content;
            } else {
                throw new IllegalArgumentException("URL content is not a JBoss VFS VirtualFile. Type is: "
                                                   + (content == null ? "null" : content.getClass().getName()));
            }
        } catch (IOException e) {
            throw new RuntimeException("could not instantiate VFS directory", e);
        }
    }

    @Override
    public String getPath() {
        return virtualFile.getPathName();
    }

    @Override
    public Iterable<Vfs.File> getFiles() {
        return new Iterable<Vfs.File>() {
            @Override
            public Iterator<Vfs.File> iterator() {
                final List<VirtualFile> toVisit = new ArrayList<VirtualFile>(virtualFile.getChildren());

                return new AbstractIterator<Vfs.File>() {

                    @Override
                    protected Vfs.File computeNext() {
                        while (!toVisit.isEmpty()) {
                            final VirtualFile nextFile = toVisit.remove(toVisit.size() - 1);
                            if (nextFile.isDirectory()) {
                                toVisit.addAll(nextFile.getChildren());
                                continue;
                            }
                            return new Vfs.File() {
                                @Override
                                public String getName() {
                                    return nextFile.getName();
                                }

                                @Override
                                public String getRelativePath() {
                                    return nextFile.getPathNameRelativeTo(virtualFile);
                                }

                                @Override
                                public InputStream openInputStream() throws IOException {
                                    return nextFile.openStream();
                                }
                            };
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return virtualFile.getName();
    }
}
