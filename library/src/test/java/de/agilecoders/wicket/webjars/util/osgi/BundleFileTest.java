package de.agilecoders.wicket.webjars.util.osgi;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class BundleFileTest extends BundleTestBase {

	private BundleFile bundleFile;
	private BundleDir mockBundleDir;

	@Before
	public void before() {

		mockBundleDir = mock(BundleDir.class);
		bundleFile = new BundleFile(mockBundleDir, createURL("/dir/dir2/file"));
	}

	@Test
	public void testGettingRelativePath() {
		when(mockBundleDir.getPath()).thenReturn("/");
		assertThat(bundleFile.getRelativePath(), equalTo("dir/dir2/file"));
	}
	
	@Test
	public void testGettingFileName() {
		when(mockBundleDir.getPath()).thenReturn("/");
		assertThat(bundleFile.getName(), equalTo("file"));
	}
}
