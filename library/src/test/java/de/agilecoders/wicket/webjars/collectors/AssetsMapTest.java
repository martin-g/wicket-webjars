package de.agilecoders.wicket.webjars.collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import de.agilecoders.wicket.webjars.settings.WebjarsSettings;

public class AssetsMapTest extends Assertions {

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
                Set<String> assets = new HashSet<String>();
                assets.add("/webjars/realname/3.0.0/prefix.realname.js");
                assets.add("/webjars/realname/2.0.0/realname.js");
                return assets;
            }
        };
        String versionFor = assetsMap.findRecentVersionFor("realname/current/realname.js");
        assertThat(versionFor, is(equalTo("2.0.0")));
    }
}
