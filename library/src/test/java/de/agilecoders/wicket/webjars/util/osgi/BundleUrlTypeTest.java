package de.agilecoders.wicket.webjars.util.osgi;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class BundleUrlTypeTest extends BundleTestBase {

	private BundleUrlType urlType;

	@Before
	public void before() {
		urlType = new BundleUrlType();
	}

	@Test
	public void testAcceptableUrlProtocols() {
		try {

			URL bundleUrl = createURL("bundle", "file");
			URL bundleResourceUrl = createURL("bundleresource", "file");
			URL invalidUrl = createURL("INVALID", "file");

			assertThat(urlType.matches(bundleUrl), is(true));
			assertThat(urlType.matches(bundleResourceUrl), is(true));
			assertThat(urlType.matches(invalidUrl), is(false));

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testNoOSGiRuntimeGetDir() throws Exception {
		URL bundleUrl = createURL("bundle", "file");
		assertThat(urlType.createDir(bundleUrl), nullValue());
	}
}
