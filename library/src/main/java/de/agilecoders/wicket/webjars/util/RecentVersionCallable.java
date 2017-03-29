package de.agilecoders.wicket.webjars.util;

import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.collectors.AssetsMap;
import de.agilecoders.wicket.webjars.collectors.IRecentVersionProvider;
import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
        return new FutureTask<String>(new RecentVersionCallable(partialPath));
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
        return Holder.getRecentVersionProvider().findRecentVersionFor(partialPath);
    }

    static final class Holder {
        private static IRecentVersionProvider recentVersionProvider;

        static void createNewRecentVersionProvider(IWebjarsSettings settings) {
            recentVersionProvider = new AssetsMap(settings);
        }

        static IRecentVersionProvider getRecentVersionProvider() {
            if (recentVersionProvider == null)
                recentVersionProvider = new AssetsMap(WicketWebjars.settings());
            return recentVersionProvider;
        }
    }
}