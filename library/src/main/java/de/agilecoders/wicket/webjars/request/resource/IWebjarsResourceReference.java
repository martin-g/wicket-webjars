package de.agilecoders.wicket.webjars.request.resource;

/**
 * Marker interface for webjars resource references.
 *
 * @author miha
 */
public interface IWebjarsResourceReference {

    /**
     * @return original name of webjars resource before resolving it
     */
    String getOriginalName();
}
