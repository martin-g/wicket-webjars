package de.agilecoders.wicket.webjars.collectors;

import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * An {@link de.agilecoders.wicket.webjars.collectors.AssetPathCollector} collects webjars assets from
 * an url/classpath/disc and so on depending on the protocol that is used.
 *
 * @author miha
 */
public interface AssetPathCollector {

    /**
     * whether this collector supports given url (especially protocol)
     *
     * @param url the url to webjars asset
     * @return true, if given protocol is accepted
     */
    boolean accept(URL url);

    /**
     * collects all webjars assets on given url.
     *
     * @param url        the path to webjars assets
     * @param filterExpr a filter that must be applied on all found assets.
     * @return a collection of webjars assets on given {@code url} that matches given {@code filterExpr}
     */
    Collection<String> collect(URL url, Pattern filterExpr);
}
