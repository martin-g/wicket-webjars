package de.agilecoders.wicket.webjars.request.resource;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.Locale;

import static de.agilecoders.wicket.webjars.util.WebjarsVersion.useRecent;

/**
 * Static resource reference for javascript webjars resources. The resources are filtered (stripped comments
 * and whitespace) if there is a registered compressor.
 * <p>
 * You are able find out how a specific name looks like on http://www.webjars.org/.
 * </p>
 *
 * @author miha
 */
public class WebjarsJavaScriptResourceReference extends JavaScriptResourceReference implements IWebjarsResourceReference {

    private final String originalName;

    /**
     * Construct.
     *
     * @param name The webjars path to load
     */
    public WebjarsJavaScriptResourceReference(final String name) {
        super(WebjarsJavaScriptResourceReference.class, useRecent(name));

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
        return "[webjars js resource] " + getOriginalName() + " (resolved name: " + getName() + ")";
    }
}
