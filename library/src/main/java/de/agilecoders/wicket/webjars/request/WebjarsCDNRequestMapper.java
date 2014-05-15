package de.agilecoders.wicket.webjars.request;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.handler.resource.ResourceReferenceRequestHandler;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.Strings;

import de.agilecoders.wicket.webjars.request.resource.IWebjarsResourceReference;

/**
 * Maps {@link ResourceReference}s of type {@link IWebjarsResourceReference} to the WebJar CDN URLs.
 * Based on de.agilecoders.wicket.extensions.request.StaticResourceRewriteMapper.
 */
public class WebjarsCDNRequestMapper implements IRequestMapper {

	private static final String WEBJAR_PATH = "/webjars/";
	
	private final IRequestMapper chain;
	private final String webJarCdnUrl;

	public WebjarsCDNRequestMapper(final IRequestMapper chain,
			final String cdnUrl) {
		this.chain = chain;
		this.webJarCdnUrl = cdnUrl;
	}

	@Override
	public Url mapHandler(final IRequestHandler requestHandler) {
		if (requestHandler instanceof ResourceReferenceRequestHandler) {
			final ResourceReferenceRequestHandler resourceReferenceRequestHandler = (ResourceReferenceRequestHandler) requestHandler;
			final ResourceReference resourceReference = resourceReferenceRequestHandler
					.getResourceReference();
			if (resourceReference instanceof IWebjarsResourceReference) {
				final Url url = chain.mapHandler(requestHandler);
				final String urlString = url.toString();
				final int index = urlString.indexOf(WEBJAR_PATH);
				if (index >= 0) {
					return Url.parse(Strings.join("/", webJarCdnUrl,
							urlString.substring(index + WEBJAR_PATH.length())));
				} else {
					return url;
				}
			}
		}
		return null;
	}

	@Override
	public IRequestHandler mapRequest(final Request request) {
		return null;
	}

	@Override
	public int getCompatibilityScore(final Request request) {
		return 0;
	}

}
