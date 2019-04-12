package de.agilecoders.wicket.webjars;

import de.agilecoders.wicket.webjars.request.WebjarsCDNRequestMapper;
import de.agilecoders.wicket.webjars.settings.IWebjarsSettings;
import de.agilecoders.wicket.webjars.settings.WebjarsSettings;
import de.agilecoders.wicket.webjars.util.WebjarsVersion;
import de.agilecoders.wicket.webjars.util.file.WebjarsResourceFinder;
import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.core.request.mapper.ResourceReferenceMapper;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.util.file.IResourceFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

/**
 * Helper class for webjars resources
 *
 * @author miha
 */
public final class WicketWebjars {
    private static final Logger LOG = LoggerFactory.getLogger("wicket-webjars");

    /**
     * The {@link org.apache.wicket.MetaDataKey} used to retrieve the {@link IWebjarsSettings} from the Wicket {@link Appendable}.
     */
    private static final MetaDataKey<IWebjarsSettings> WEBJARS_SETTINGS_METADATA_KEY = new MetaDataKey<IWebjarsSettings>() {
    };

    /**
     * Checks whether Webjars support is already installed
     *
     * @param application the wicket application
     * @return {@code true} if Webjars is already installed, otherwise {@code false}
     */
    public static boolean isInstalled(Application application) {
        return application.getMetaData(WEBJARS_SETTINGS_METADATA_KEY) != null;
    }

    /**
     * installs the webjars resource finder and uses a set of default settings.
     *
     * @param app the wicket application
     */
    public static void install(final WebApplication app) {
        install(app, null);
    }

    /**
     * installs the webjars resource finder
     *
     * @param app      the wicket application
     * @param settings the settings to use
     */
    public static void install(WebApplication app, IWebjarsSettings settings) {
        final IWebjarsSettings existingSettings = settings(app);

        if (existingSettings == null) {
            if (settings == null) {
                settings = new WebjarsSettings();
            }

            app.setMetaData(WEBJARS_SETTINGS_METADATA_KEY, settings);

            if (settings.useCdnResources()) {
                mountCDNMapper(app, settings.cdnUrl());
            }

            final List<IResourceFinder> finders = app.getResourceSettings().getResourceFinders();
            final WebjarsResourceFinder finder = new WebjarsResourceFinder(settings);

            if (!finders.contains(finder)) {
                finders.add(finder);
            }

            LOG.info("initialize wicket webjars with given settings: {}", settings);
        }
    }

    public static void reindex(final WebApplication application) {
        final List<IResourceFinder> resourceFinders = application.getResourceSettings().getResourceFinders();
        for (IResourceFinder resourceFinder : resourceFinders) {
            if (resourceFinder instanceof WebjarsResourceFinder) {
                WebjarsVersion.reset();
                WebjarsResourceFinder webjarsResourceFinder = (WebjarsResourceFinder) resourceFinder;
                webjarsResourceFinder.reindex();
                break;
            }
        }
    }

    /**
     * mounts a special resource reference mapper that transform webjars resource urls into a cdn url.
     *
     * @param app    current web app
     * @param cdnUrl the cdn url to use
     */
    private static void mountCDNMapper(final WebApplication app, String cdnUrl) {
        Supplier<String> parentFolderPlaceholderProvider = () -> app.getResourceSettings().getParentFolderPlaceholder();
        Supplier<IResourceCachingStrategy> cachingStrategyProvider = () -> app.getResourceSettings().getCachingStrategy();

        LOG.info("use cdn resources from {}", cdnUrl);

        IRequestMapper delegate = new ResourceReferenceMapper(new PageParametersEncoder(), parentFolderPlaceholderProvider, cachingStrategyProvider);
        app.mount(new WebjarsCDNRequestMapper(delegate, cdnUrl, cachingStrategyProvider));
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
            final IWebjarsSettings settings = Application.get().getMetaData(WEBJARS_SETTINGS_METADATA_KEY);

            if (settings != null) {
                return settings;
            } else {
                throw new IllegalStateException("you have to call WicketWebjars.install() before you can use an "
                                                + "IWebjarsResourceReference or any other component.");
            }
        }

        final String warning = "There is no Wicket Application thread local! Going to use default Webjars settings.";
        LOG.warn(warning, new RuntimeException(warning));
        return new WebjarsSettings();
    }

    /**
     * private constructor.
     */
    private WicketWebjars() {
        throw new UnsupportedOperationException();
    }
}
