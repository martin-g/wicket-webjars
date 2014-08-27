package de.agilecoders.wicket.webjars;

import static org.hamcrest.CoreMatchers.is;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WicketWebjarsTest extends Assert {

    private WicketTester tester;

    @Before
    public void setUp() throws Exception {
        tester = new WicketTester();
    }

    @After
    public void tearDown() throws Exception {
        tester.destroy();
    }

    @Test
    public void isInstalled() throws Exception {
        WebApplication application = tester.getApplication();


        assertThat(WicketWebjars.isInstalled(application), is(false));

        WicketWebjars.install(application);
        assertThat(WicketWebjars.isInstalled(application), is(true));
    }
}
