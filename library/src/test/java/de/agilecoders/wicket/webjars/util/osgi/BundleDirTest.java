package de.agilecoders.wicket.webjars.util.osgi;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.reflections.vfs.Vfs.File;

public class BundleDirTest extends BundleTestBase {

	private BundleDir bundleDir;
	private Bundle mockBundle;

	@Before
	public void before() {
		mockBundle = mock(Bundle.class);
		bundleDir = new BundleDir(mockBundle, createURL("someFile"));
	}

	@Test
	public void testGettingPath() {
		assertThat(bundleDir.getPath(), is("someFile"));
	}

	@Test
	public void testGetFileFromUrlNullEnumeration() {
		Iterable<File> files = bundleDir.getFiles();

		assertThat(countFiles(files), is(0));
	}

	@Test
	public void testGetFileFromUrlFilesAndDirsFound() {

		List<URL> urls = new ArrayList<URL>();
		urls.add(createURL("someFile"));
		urls.add(createURL("someDir/"));
		urls.add(createURL("someOtherFile"));

		Enumeration<URL> urlEnumeration = Collections.enumeration(urls);

		when(mockBundle.findEntries("", "*", true)).thenReturn(urlEnumeration);

		Iterable<File> files = bundleDir.getFiles();

		assertThat(countFiles(files), is(2));
	}

	@Test
	public void testGetFileFromUrlOnlyDirsFound() {

		List<URL> urls = new ArrayList<URL>();
		urls.add(createURL("someDir/"));
		urls.add(createURL("someOtherDir/"));

		Enumeration<URL> urlEnumeration = Collections.enumeration(urls);

		when(mockBundle.findEntries("", "*", true)).thenReturn(urlEnumeration);

		Iterable<File> files = bundleDir.getFiles();

		assertThat(countFiles(files), is(0));
	}

	private int countFiles(Iterable<File> files) {
		int count = 0;
		for (File file : files) {
			count++;
		}

		return count;
	}
}
