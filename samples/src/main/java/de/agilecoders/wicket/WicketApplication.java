package de.agilecoders.wicket;

import com.google.common.collect.Sets;
import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.VfsJarAssetPathCollector;
import de.agilecoders.wicket.webjars.settings.WebjarsSettings;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import java.util.Set;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends WebApplication {
    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends WebPage> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        WebjarsSettings settings = new WebjarsSettings();

        Set<AssetPathCollector> collectors = Sets.newHashSet(settings.assetPathCollectors());
        collectors.add(new VfsJarAssetPathCollector());

        settings.assetPathCollectors(collectors.toArray(new AssetPathCollector[collectors.size()]));

        WicketWebjars.install(this, settings);
    }
}
