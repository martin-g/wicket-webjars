package de.agilecoders.wicket.webjars.util.file;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for WebjarsResourceFinder
 */
public class WebjarsResourceFinderTest extends Assert {

    /**
     * https://github.com/l0rdn1kk0n/wicket-bootstrap/issues/280
     *
     * Return {@code null} for missing resources
     */
    @Test
    public void find() {
        WebjarsResourceFinder finder = new WebjarsResourceFinder();

        assertNull(finder.find(String.class, "non existing"));
    }

}
