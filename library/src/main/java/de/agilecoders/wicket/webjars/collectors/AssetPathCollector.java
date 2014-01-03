package de.agilecoders.wicket.webjars.collectors;

import java.net.URL;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * TODO miha: document class purpose
 * 
 * @author miha
 */
public interface AssetPathCollector {

	boolean accept(URL url);

	Collection<String> collect(URL url, Pattern filterExpr);
}
