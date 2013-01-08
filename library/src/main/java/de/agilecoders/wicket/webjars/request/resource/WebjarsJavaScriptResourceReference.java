package de.agilecoders.wicket.webjars.request.resource;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

import java.util.Locale;

import static de.agilecoders.wicket.webjars.util.WebjarsVersion.useRecent;
import static de.agilecoders.wicket.webjars.util.WicketWebjars.prependWebjarsPathIfMissing;

/**
 * Static resource reference for javascript webjars resources. The resources are filtered (stripped comments
 * and whitespace) if there is a registered compressor.
 * <p/>
 * You are able find out how a specific name looks like on http://www.webjars.org/.
 *
 * @author miha
 */
public class WebjarsJavaScriptResourceReference extends JavaScriptResourceReference {

    private final String originalName;

    /**
     * Construct.
     *
     * @param name The webjars path to load
     */
    public WebjarsJavaScriptResourceReference(final String name) {
        super(WebjarsJavaScriptResourceReference.class, useRecent(prependWebjarsPathIfMissing(name)));

        this.originalName = name;
    }

    /**
     * @return original name of webjars resource before resolving it
     */
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
