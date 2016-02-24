package de.agilecoders.wicket.webjars.collectors;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import de.agilecoders.wicket.webjars.util.WebJarAssetLocator;

/**
 * A {@link de.agilecoders.wicket.webjars.collectors.FileAssetPathCollector} searches webjars on disk
 * in a special directory.
 * 
 * @author miha
 */
public class FileAssetPathCollector extends ProtocolAwareAssetPathCollector {

    private final String pathPrefix;

    /**
     * Construct.
     *
     * @param pathPrefix the path where to look for resources
     */
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
            throw new WebJarAssetLocator.ResourceException(url.toString(), e.getMessage());
        }

        return listFiles(file, filterExpr);
    }

    /**
     * Recursively search all directories for relative file paths matching `filterExpr`.
     *
     * @param file       the directory to search in
     * @param filterExpr the filter to apply
     * @return all files that matches given filter
     */
    private Set<String> listFiles(final File file, final Pattern filterExpr) {
        final Set<String> aggregatedChildren = new HashSet<>();
        aggregateChildren(file, aggregatedChildren, filterExpr);
        return aggregatedChildren;
    }

    private void aggregateChildren(final File file, final Set<String> aggregatedChildren, final Pattern filterExpr) {
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (final File child : files) {
                    aggregateChildren(child, aggregatedChildren, filterExpr);
                }
            }
        } else if (file != null) {
            aggregateFile(file, aggregatedChildren, filterExpr);
        }
    }

    private void aggregateFile(final File file, final Set<String> aggregatedChildren, final Pattern filterExpr) {
        final String path = file.getPath().replace('\\', '/');
        final String relativePath = path.substring(path.indexOf(pathPrefix));

        if (filterExpr.matcher(relativePath).matches()) {
            aggregatedChildren.add(relativePath);
        }
    }
}
