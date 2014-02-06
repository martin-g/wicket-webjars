package de.agilecoders.wicket.webjars;

import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import de.agilecoders.wicket.webjars.settings.WebjarsSettings;
import de.agilecoders.wicket.webjars.util.file.WebjarsResourceFinder;
import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.util.file.IResourceFinder;

import java.util.List;

/**
 * Helper class for webjars resources
 *
 * @author miha
 */
public final class WicketWebjars {
    private static final String PATH_SPLITTER = "/";
    private static WebjarsResourceFinder FINDER = null;

    /**
     * The {@link org.apache.wicket.MetaDataKey} used to retrieve the {@link IWebjarsSettings} from the Wicket {@link Appendable}.
     */
    private static final MetaDataKey<IWebjarsSettings> WEBJARS_SETTINGS_METADATA_KEY = new MetaDataKey<IWebjarsSettings>() {
    };

    /**
     * @return the webjars resource finder
     */
    public static WebjarsResourceFinder finder() {
        return FINDER;
    }

    /**
     * installs the webjars resource finder and uses a set of default settings.
     *
     * @param app the wicket application
     */
    public static void install(final Application app) {
        install(app, new WebjarsSettings());
    }

    /**
     * installs the webjars resource finder
     *
     * @param app      the wicket application
     * @param settings the settings to use
     */
    public static void install(final Application app, IWebjarsSettings settings) {
        final IWebjarsSettings existingSettings = settings(app);

        if (existingSettings == null) {
            app.setMetaData(WEBJARS_SETTINGS_METADATA_KEY, settings);

            final List<IResourceFinder> finders = app.getResourceSettings().getResourceFinders();
            final WebjarsResourceFinder finder = new WebjarsResourceFinder(settings);

            if (!finders.contains(finder)) {
                finders.add(finder);
                FINDER = finder;
            }
        }
    }

    /**
     * returns the {@link IWebjarsSettings} which are assigned to given application
     *
     * @param app The current application
     * @return assigned {@link IWebjarsSettings}
     */
    public static IWebjarsSettings settings(final Application app) {
        return app.getMetaData(WEBJARS_SETTINGS_METADATA_KEY);
    }

    /**
     * returns the {@link IWebjarsSettings} which are assigned to current application
     *
     * @return assigned {@link IWebjarsSettings}
     */
    public static IWebjarsSettings settings() {
        if (Application.exists()) {
            return settings(Application.get());
        }

        throw new IllegalStateException("there is no active application assigned to this thread.");
    }

    /**
     * private constructor.
     */
    private WicketWebjars() {
        throw new UnsupportedOperationException();
    }
}
