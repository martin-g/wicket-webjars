package de.agilecoders.wicket.webjars.request;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.ResourceUrl;
import org.apache.wicket.util.string.Strings;

import java.util.List;
import java.util.function.Supplier;

import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;
import de.agilecoders.wicket.webjars.util.Helper;

/**
 * Maps {@link ResourceReference}s of type {@link IWebjarsResourceReference} to
 * the WebJar CDN URLs. Based on
 * de.agilecoders.wicket.extensions.request.StaticResourceRewriteMapper.
 */
public class WebjarsCDNRequestMapper implements IRequestMapper {

    private final IRequestMapper chain;
    private final String webJarCdnUrl;
    private final Supplier<IResourceCachingStrategy> cachingStrategyProvider;

    public WebjarsCDNRequestMapper(final IRequestMapper chain,
                                   final String cdnUrl, final Supplier<IResourceCachingStrategy> cachingStrategyProvider) {
        this.chain = chain;
        this.webJarCdnUrl = cdnUrl;
        this.cachingStrategyProvider = cachingStrategyProvider;
    }

    @Override
    public Url mapHandler(final IRequestHandler requestHandler) {
        if (isWebjarsResourceReference(requestHandler)) {
            final Url url = chain.mapHandler(requestHandler);
            final String urlString = urlToStringWithNoVersion(url);
            final int index = urlString.indexOf(Helper.PATH_PREFIX);

            if (index >= 0) {
                return Url.parse(Strings.join("/", webJarCdnUrl,
                                              urlString.substring(index + Helper.PATH_PREFIX.length())));
            } else {
                return url;
            }
        }

        return null;
    }

    private static boolean isWebjarsResourceReference(final IRequestHandler requestHandler) {
        if (requestHandler instanceof ResourceReferenceRequestHandler) {
            final ResourceReferenceRequestHandler resourceReferenceRequestHandler = (ResourceReferenceRequestHandler) requestHandler;
            final ResourceReference resourceReference = resourceReferenceRequestHandler.getResourceReference();

            if (resourceReference instanceof IWebjarsResourceReference) {
                return true;
            }
        }

        return false;
    }

    @Override
    public IRequestHandler mapRequest(final Request request) {
        return null;
    }

    @Override
    public int getCompatibilityScore(final Request request) {
        return 0;
    }

    /**
     * @param url to remove version from
     * @return the string representation of the {@link Url} with any version info removed
     */
    private String urlToStringWithNoVersion(final Url url) {
        final Url copy = new Url(url);
        final List<String> segments = copy.getSegments();

        if (!segments.isEmpty()) {
            final int lastSegmentIndex = segments.size() - 1;
            final String filename = segments.get(lastSegmentIndex);

            if (!Strings.isEmpty(filename)) {
                final ResourceUrl resourceUrl = new ResourceUrl(filename, new PageParameters());

                cachingStrategyProvider.get().undecorateUrl(resourceUrl);

                if (Strings.isEmpty(resourceUrl.getFileName())) {
                    throw new IllegalStateException(
                            "caching strategy returned empty name for "
                            + resourceUrl);
                }

                segments.set(lastSegmentIndex, resourceUrl.getFileName());
            }
        }

        return copy.toString();
    }

}
