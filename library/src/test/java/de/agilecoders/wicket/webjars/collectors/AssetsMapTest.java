package de.agilecoders.wicket.webjars.collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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

    /**
     * https://github.com/martin-g/wicket-webjars/issues/167
     * 
     * Matching was done on partial path-component, so bootstrap4 resources matched bootstrap resource lookup.
     */
    @Test
    public void partialPathMatching() {
        AssetsMap assetsMap = new AssetsMap(new WebjarsSettings()) {
            public SortedMap<String, String> getFullPathIndex() {
            	// only values are significant for the use case
            	// same versions must be used to triggers the issue as versions are put in a Set and first version
            	// is retrieved.
                return new TreeMap<>(Map.of(
                		"0", "META-INF/resources/webjars/bootstrap4/4.6.0/matching.js",
                		"1", "META-INF/resources/webjars/bootstrap/5.3.2/matching.js"
                ));
            }
        };
        String versionFor = assetsMap.findRecentVersionFor("/bootstrap/current/matching.js");
        // bootstrap4 must not match
        assertThat(versionFor, is(equalTo("5.3.2")));
    }
}
