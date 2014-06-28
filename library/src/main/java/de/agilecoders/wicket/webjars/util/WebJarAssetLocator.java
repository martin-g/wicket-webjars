package de.agilecoders.wicket.webjars.util;

import de.agilecoders.wicket.webjars.collectors.AssetsMap;
import de.agilecoders.wicket.webjars.collectors.IAssetProvider;
import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import org.apache.wicket.util.lang.Args;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import static de.agilecoders.wicket.webjars.util.Helper.reversePath;

/**
 * Locate WebJar assets. The class is thread safe.
 */
public class WebJarAssetLocator implements IAssetProvider, IFullPathProvider {
    private final AssetsMap assetMap;
    private final String recentVersionPlaceHolder;

    /**
     * Convenience constructor that will form a locator for all resources on the current class path.
     */
    public WebJarAssetLocator(final IWebjarsSettings settings) {
        this.assetMap = new AssetsMap(settings);
        this.recentVersionPlaceHolder = "/" + settings.recentVersionPlaceHolder() + "/";
    }

    private String throwNotFoundException(final String partialPath) {
        throw new ResourceException(partialPath, partialPath + " could not be found. Make sure you've added the "
                                                 + "corresponding WebJar and please check for typos.");
    }

    private String throwMultipleMatchesException(final String partialPath) {
        throw new ResourceException(partialPath, "Multiple matches found for " + partialPath
                                                 + ". Please provide a more specific path, for example by including a version number.");
    }

    @Override
    public String getFullPath(String partialPath) {
        partialPath = Args.notEmpty(partialPath, "partialPath").contains(recentVersionPlaceHolder) ?
                      partialPath.replace(recentVersionPlaceHolder,
                                          "/" + assetMap.findRecentVersionFor(partialPath) + "/") :
                      partialPath;

        final String reversePartialPath = reversePath(partialPath);
        final SortedMap<String, String> fullPathTail = assetMap.getFullPathIndex().tailMap(reversePartialPath);

        if (fullPathTail.size() == 0) {
            throwNotFoundException(partialPath);
        }

        final Iterator<Entry<String, String>> fullPathTailIter = fullPathTail.entrySet().iterator();
        final Entry<String, String> fullPathEntry = fullPathTailIter.next();
        if (!fullPathEntry.getKey().startsWith(reversePartialPath)) {
            throwNotFoundException(partialPath);
        }
        final String fullPath = fullPathEntry.getValue();

        if (fullPathTailIter.hasNext() && fullPathTailIter.next().getKey().startsWith(reversePartialPath)) {
            throwMultipleMatchesException(reversePartialPath);
        }

        return fullPath;
    }

    @Override
    public SortedMap<String, String> getFullPathIndex() {
        return assetMap.getFullPathIndex();
    }

    @Override
    public Set<String> listAssets(final String folderPath) {
        return assetMap.listAssets(folderPath);
    }

    /**
     * resource exception without stacktrace.
     */
    public static class ResourceException extends RuntimeException {

        private final String resource;

        /**
         * Construct.
         *
         * @param resource the resource
         * @param message  error message
         */
        public ResourceException(String resource, String message) {
            super(message);

            this.resource = resource;
        }

        public String resource() {
            return resource;
        }

        @Override
        public synchronized Throwable getCause() {
            return null;
        }

        @Override
        public synchronized Throwable initCause(Throwable cause) {
            return this;
        }

        @Override
        public void printStackTrace() {
            // nothing to do
        }

        @Override
        public void printStackTrace(PrintStream s) {
            printStackTrace();
        }

        @Override
        public void printStackTrace(PrintWriter s) {
            printStackTrace();
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }

        @Override
        public StackTraceElement[] getStackTrace() {
            return new StackTraceElement[] { };
        }

        @Override
        public void setStackTrace(StackTraceElement[] stackTrace) {
            // nothing to do
        }
    }
}
