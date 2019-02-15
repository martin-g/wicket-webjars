package de.agilecoders.wicket.webjars;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WicketWebjarsTest extends Assertions {

    private WicketTester tester;

    @BeforeEach
    void setUp() throws Exception {
        tester = new WicketTester();
    }

    @AfterEach
    void tearDown() throws Exception {
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
