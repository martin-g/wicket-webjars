package de.agilecoders.wicket.webjars.collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.agilecoders.wicket.webjars.settings.WebjarsSettings;

public class AssetsMapTest extends Assert{

    /**
     * https://github.com/l0rdn1kk0n/wicket-webjars/issues/22
     * 
     * Parse the version of the correct asset when there is an asset
     * with a similar name but with a prefix
     */
    @Test
    public void correctVersion()
    {
        AssetsMap assetsMap = new AssetsMap(new WebjarsSettings()) {
            @Override
            public Set<String> listAssets(String folderPath) {
                return new HashSet<String>(Arrays.asList("/webjars/realname/3.0.0/prefix.realname.js", "/webjars/realname/2.0.0/realname.js"));
            }
        };
        String versionFor = assetsMap.findRecentVersionFor("realname/current/realname.js");
        assertThat(versionFor, is(equalTo("2.0.0")));
    }
}
