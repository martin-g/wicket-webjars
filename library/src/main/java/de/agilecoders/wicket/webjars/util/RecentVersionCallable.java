package de.agilecoders.wicket.webjars.util;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.collectors.AssetsMap;

/**
 * Callable that loads the recent version string of given webjars resource
 *
 * @author miha
 */
public class RecentVersionCallable implements Callable<String> {

    /**
     * creates a new future recent version collector
     *
     * @param partialPath the resource path
     * @return recent version as future
     */
    public static FutureTask<String> createFutureTask(final String partialPath) {
        return new FutureTask<>(new RecentVersionCallable(partialPath));
    }

    private final String partialPath;

    /**
     * Construct.
     *
     * @param partialPath Path to webjars resource
     */
    private RecentVersionCallable(final String partialPath) {
        this.partialPath = partialPath;
    }

    @Override
    public String call() throws Exception {
        return collectRecentVersionFor(partialPath);
    }

    /**
     * collects recent version string of given webjars resource from classpath.
     *
     * @param partialPath The webjars resource path
     * @return recent version string
     */
    private static String collectRecentVersionFor(final String partialPath) {
        return Holder.recentVersionProvider.findRecentVersionFor(partialPath);
    }

    static final class Holder {
        static final AssetsMap recentVersionProvider = new AssetsMap(WicketWebjars.settings());
    }
}
