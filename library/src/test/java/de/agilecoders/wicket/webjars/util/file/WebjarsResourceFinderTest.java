package de.agilecoders.wicket.webjars.util.file;

import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static de.agilecoders.wicket.webjars.util.WebjarsVersion.useRecent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;

/**
 * Tests for WebjarsResourceFinder
 */
public class WebjarsResourceFinderTest extends Assert {

    private WicketTester tester;

    /**
     * https://github.com/l0rdn1kk0n/wicket-bootstrap/issues/280
     *
     * Return {@code null} for missing resources
     */
    @Test
    public void findNonExistingFile() {
        WebjarsResourceFinder finder = new WebjarsResourceFinder(WicketWebjars.settings());

        assertNull(finder.find(String.class, "non existing"));
    }

    /**
     * https://github.com/l0rdn1kk0n/wicket-webjars/issues/20
     *
     * Return {@code null} for missing resources
     */
    @Test
    public void findWithNullScope() {
        WebjarsResourceFinder finder = new WebjarsResourceFinder(WicketWebjars.settings());

        assertNull(finder.find(null, "non existing"));
    }

    @Test
    public void findOnGAE() throws ResourceStreamNotFoundException, IOException {
        System.setProperty("com.google.appengine.runtime.environment", "Production");

        WebjarsResourceFinder finder = new WebjarsResourceFinder(WicketWebjars.settings());
        IResourceStream stream = finder.find(IWebjarsResourceReference.class, "/webjars/jquery/2.2.1/jquery.min.js");

        System.setProperty("com.google.appengine.runtime.environment", "");

        assertThat(stream, is(not(nullValue())));
        assertThat(IOUtils.toString(stream.getInputStream()), startsWith("/*! jQuery v2.2.1"));
    }

    @Test
    public void findFile() throws ResourceStreamNotFoundException, IOException {
        WebjarsResourceFinder finder = new WebjarsResourceFinder(WicketWebjars.settings());
        IResourceStream stream = finder.find(IWebjarsResourceReference.class, "/webjars/jquery/2.2.1/jquery.min.js");

        assertThat(stream, is(not(nullValue())));
        assertThat(IOUtils.toString(stream.getInputStream()), startsWith("/*! jQuery v2.2.1"));
    }

    @Test
    public void findFileWithoutVersion() throws ResourceStreamNotFoundException, IOException {
        WebjarsResourceFinder finder = new WebjarsResourceFinder(WicketWebjars.settings());
        IResourceStream stream = finder.find(IWebjarsResourceReference.class,
                                             useRecent("/webjars/jquery/current/jquery.min.js"));

        assertThat(stream, is(not(nullValue())));
        assertThat(IOUtils.toString(stream.getInputStream()), startsWith("/*! jQuery v2.2.1"));
    }

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester(new MockApplication() {
            @Override
            protected void init() {
                super.init();

                WicketWebjars.install(this);
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        tester.destroy();
    }
}
