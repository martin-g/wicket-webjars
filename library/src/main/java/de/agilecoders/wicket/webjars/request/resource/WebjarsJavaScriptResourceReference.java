package de.agilecoders.wicket.webjars.request.resource;

import org.apache.wicket.request.resource.JavaScriptResourceReference;
import java.util.Locale;
import static de.agilecoders.wicket.webjars.util.Webjars.prependWebjarsPathIfMissing;

/**
 * Static resource reference for javascript webjars resources. The resources are filtered (stripped comments
 * and whitespace) if there is a registered compressor.
 *
 * You are able find out how a specific path looks like on http://www.webjars.org/.
 *
 * @author miha
 */
public class WebjarsJavaScriptResourceReference extends JavaScriptResourceReference {

    /**
     * Construct.
     *
     * @param path The webjars path to load
     */
    public WebjarsJavaScriptResourceReference(final String path) {
        super(WebjarsJavaScriptResourceReference.class, prependWebjarsPathIfMissing(path));
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public String getStyle() {
        return null;
    }

    @Override
    public String getVariation() {
        return null;
    }

}
