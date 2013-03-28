package de.agilecoders.wicket.webjars.util.osgi;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

public class BundleTestBase {

	private URLStreamHandler mockStreamHandler;

	public BundleTestBase() {
		mockStreamHandler = mock(URLStreamHandler.class);
	}

	protected URL createURL(String protocol, String file) {
		URL url = null;

		try {
			url = new URL(protocol, "host", 0, file, mockStreamHandler);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		return url;
	}

	protected URL createURL(String file) {
		return createURL("protocol", file);
	}

}
