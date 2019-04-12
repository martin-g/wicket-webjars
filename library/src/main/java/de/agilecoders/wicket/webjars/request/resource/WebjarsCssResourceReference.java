package de.agilecoders.wicket.webjars.request.resource;

import static de.agilecoders.wicket.webjars.util.Helper.prependWebjarsPathIfMissing;
import static de.agilecoders.wicket.webjars.util.WebjarsVersion.useRecent;

import java.util.Locale;

import org.apache.wicket.request.resource.CssResourceReference;

/**
 * Static resource reference for webjars css resources. The resources are filtered (stripped comments and
 * whitespace) if there is registered compressor.
 * <p>
 * You are able find out how a specific path looks like on http://www.webjars.org/.
 * </p>
 *
 * @author miha
 */
public class WebjarsCssResourceReference extends CssResourceReference implements IWebjarsResourceReference {

    private final String originalName;

    /**
     * Construct.
     *
     * @param name The webjars path to load
     */
    public WebjarsCssResourceReference(final String name) {
        super(WebjarsCssResourceReference.class, useRecent(prependWebjarsPathIfMissing(name)));

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
        return "[webjars css resource] " + getOriginalName() + " (resolved name: " + getName() + ")";
    }

}
