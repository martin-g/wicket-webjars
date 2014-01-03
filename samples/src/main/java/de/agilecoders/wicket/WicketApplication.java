package de.agilecoders.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import de.agilecoders.wicket.webjars.collectors.VfsJarAssetPathCollector;
import de.agilecoders.wicket.webjars.util.WicketWebjars;

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

		WicketWebjars.install(this);
		WicketWebjars.registerCollector(new VfsJarAssetPathCollector());
	}
}
