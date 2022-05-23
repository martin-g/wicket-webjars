package de.agilecoders.wicket.webjars.request.resource;

import static de.agilecoders.wicket.webjars.util.Helper.prependWebjarsPathIfMissing;
import static de.agilecoders.wicket.webjars.util.WebjarsVersion.useRecent;

import java.util.Locale;

import org.apache.wicket.request.resource.PackageResourceReference;

/**
 * Static resource reference for webjars resources.
 * <p>
 * You are able find out how a specific path looks like on http://www.webjars.org/.
 * </p>
 *
 * @author Erik Geletti
 */
public class WebjarsPackageResourceReference extends PackageResourceReference implements IWebjarsResourceReference {

    private final String originalName;

    /**
     * Construct.
     *
     * @param name The webjars path to load
     */
    public WebjarsPackageResourceReference(final String name) {
        super(WebjarsPackageResourceReference.class, useRecent(prependWebjarsPathIfMissing(name)));

        this.originalName = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getOriginalName() {
        return originalName;
    }

    @Override
    public final Locale getLocale() {
        return null;
    }

    @Override
    public final String getStyle() {
        return null;
    }

    @Override
    public final String getVariation() {
        return null;
    }

    @Override
    public String toString() {
        return "[webjars package resource] " + getOriginalName() + " (resolved name: " + getName() + ")";
    }

}
