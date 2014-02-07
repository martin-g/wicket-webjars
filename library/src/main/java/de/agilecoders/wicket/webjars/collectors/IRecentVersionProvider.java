package de.agilecoders.wicket.webjars.collectors;

/**
 * provides access to recent version of a webjars
 *
 * @author miha
 */
public interface IRecentVersionProvider {

    /**
     * @param path the path to detect version for
     * @return recent version
     */
    String findRecentVersionFor(String path);
}
