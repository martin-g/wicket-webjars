package de.agilecoders.wicket.webjars.util.file;

import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.tester.WicketTestCase;

import java.io.IOException;

import de.agilecoders.wicket.webjars.WicketWebjars;
import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import org.junit.jupiter.api.Test;

import static de.agilecoders.wicket.webjars.util.WebjarsVersion.useRecent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for WebjarsResourceFinder
 */
public class WebjarsResourceFinderTest extends WicketTestCase {

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
        IResourceStream stream = finder.find(IWebjarsResourceReference.class, "/webjars/jquery/3.6.3/jquery.min.js");

        System.setProperty("com.google.appengine.runtime.environment", "");

        assertThat(stream, is(not(nullValue())));
        assertThat(IOUtils.toString(stream.getInputStream()), startsWith("/*! jQuery v3.6.3"));
    }

    @Test
    public void findFile() throws ResourceStreamNotFoundException, IOException {
        WebjarsResourceFinder finder = new WebjarsResourceFinder(WicketWebjars.settings());
        IResourceStream stream = finder.find(IWebjarsResourceReference.class, "/webjars/jquery/3.6.3/jquery.min.js");

        assertThat(stream, is(not(nullValue())));
        assertThat(IOUtils.toString(stream.getInputStream()), startsWith("/*! jQuery v3.6.3"));
    }

    @Test
    public void findFileWithoutVersion() throws ResourceStreamNotFoundException, IOException {
        WebjarsResourceFinder finder = new WebjarsResourceFinder(WicketWebjars.settings());
        IResourceStream stream = finder.find(IWebjarsResourceReference.class,
                                             useRecent("/webjars/jquery/current/jquery.min.js"));

        assertThat(stream, is(not(nullValue())));
        assertThat(IOUtils.toString(stream.getInputStream()), startsWith("/*! jQuery v3.6.3"));
    }

    @Override
    protected WebApplication newApplication() {
        return new MockApplication() {
            @Override
            protected void init() {
                super.init();

                WicketWebjars.install(this);
            }
        };
    }
}
