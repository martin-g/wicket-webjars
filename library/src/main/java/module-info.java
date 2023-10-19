module de.agilecoders.wicket.webjars {
	exports de.agilecoders.wicket.webjars;
	exports de.agilecoders.wicket.webjars.collectors;
	exports de.agilecoders.wicket.webjars.request;
	exports de.agilecoders.wicket.webjars.request.resource;
	exports de.agilecoders.wicket.webjars.settings;
	exports de.agilecoders.wicket.webjars.util;
	exports de.agilecoders.wicket.webjars.util.file;

	requires org.apache.wicket.core;
	requires org.apache.wicket.request;
	requires org.apache.wicket.util;

	requires emory.util.classloader;

	requires org.slf4j;
}
