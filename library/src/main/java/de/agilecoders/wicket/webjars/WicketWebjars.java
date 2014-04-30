package de.agilecoders.wicket.webjars;

import static de.agilecoders.wicket.webjars.util.WebjarsVersion.useRecent;

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.file.IResourceFinder;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import de.agilecoders.wicket.webjars.settings.WebjarsSettings;
import de.agilecoders.wicket.webjars.util.file.WebjarsResourceFinder;

/**
 * Helper class for webjars resources
 *
 * @author miha
 */
public final class WicketWebjars {

    /**
     * The {@link org.apache.wicket.MetaDataKey} used to retrieve the {@link IWebjarsSettings} from the Wicket {@link Appendable}.
     */
    private static final MetaDataKey<IWebjarsSettings> WEBJARS_SETTINGS_METADATA_KEY = new MetaDataKey<IWebjarsSettings>() {
    };

    /**
     * installs the webjars resource finder and uses a set of default settings.
     *
     * @param app the wicket application
     */
    public static void install(final Application app) {
        install(app, null);
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
            if (settings == null) {
                settings = new WebjarsSettings();
            }

            app.setMetaData(WEBJARS_SETTINGS_METADATA_KEY, settings);

            final List<IResourceFinder> finders = app.getResourceSettings().getResourceFinders();
            final WebjarsResourceFinder finder = new WebjarsResourceFinder(settings);

            if (!finders.contains(finder)) {
                finders.add(finder);
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
            IWebjarsSettings settings = Application.get().getMetaData(WEBJARS_SETTINGS_METADATA_KEY);

            if (settings != null) {
                return settings;
            } else {
                throw new IllegalStateException("you have to call WicketWebjars.install() before you can use an "
                                                + "IWebjarsResourceReference or any other component.");
            }
        }

        throw new IllegalStateException("there is no active application assigned to this thread.");
    }
    
    /**
     * returns a {@link ResourceReference} to either the appropriate CDN or application-relative webjar
     * 
     * @param name webjar name
     * @return {@link ResourceReference} for the given webjar name
     */
    public static ResourceReference newResourceReference(final String name) {
		ResourceReference resourceReference = null;
		if (settings().useCdnResources()) {
			resourceReference = new UrlResourceReference(Url.parse(IWebjarsSettings.WEB_JAR_CDN + useRecent(name)));
		} else {
			resourceReference = new WebjarsJavaScriptResourceReference(name);
		}
		return resourceReference;
    }

    /**
     * private constructor.
     */
    private WicketWebjars() {
        throw new UnsupportedOperationException();
    }
}
