package de.agilecoders.wicket.webjars.collectors;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public abstract class ProtocolAwareAssetPathCollector implements AssetPathCollector {

	private final List<String> protocols;

	protected ProtocolAwareAssetPathCollector(final String protocol) {
		this.protocols = Arrays.asList(protocol);
	}

	protected ProtocolAwareAssetPathCollector(final String... protocols) {
		this.protocols = Arrays.asList(protocols);
	}

	@Override
	public boolean accept(URL url) {
		if (url == null) {
			return false;
		}
		for (String protocol : protocols) {
			if (protocol.equalsIgnoreCase(url.getProtocol())) {
				return true;
			}
		}
		return false;
	}
}