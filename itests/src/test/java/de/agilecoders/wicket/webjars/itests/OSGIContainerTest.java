package de.agilecoders.wicket.webjars.itests;

import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.debugConfiguration;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.editConfigurationFileExtend;
import static org.apache.karaf.tooling.exam.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.File;
import java.io.IOException;

import org.apache.karaf.tooling.exam.options.configs.FeaturesCfg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.ExamSystem;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainer;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.PaxExamRuntime;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;
import org.reflections.vfs.Vfs;

import de.agilecoders.wicket.webjars.request.resource.WebjarsJavaScriptResourceReference;
import de.agilecoders.wicket.webjars.util.file.WebjarsResourceFinder;
import de.agilecoders.wicket.webjars.util.osgi.BundleUrlType;

/**
 * This test class lets us spin up an OSGi container, deploy the required
 * dependencies and then run unit test wihthin the container to check.
 * 
 * @author ben
 * 
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class OSGIContainerTest {

	@Configuration
	public Option[] config() {

		String featuresXmlPath = null;

		try {
			featuresXmlPath = new File("src/main/features/features.xml").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return options(

				// Needed to enable the junit OSGi probe
				junitBundles(),

				// Set up Apache Karaf as the OSGi container. Note this gets the
				// ZIP from Maven, so including it in the pom ensures it in the
				// local repo speeding up the tests
				karafDistributionConfiguration().frameworkUrl(maven("org.apache.karaf", "apache-karaf").type("zip").versionAsInProject()).karafVersion("3.2.1").name("Apache Karaf"),

				//Install the feaures in our features.xml file
				editConfigurationFileExtend(FeaturesCfg.BOOT, ",supportingLibs,wicket,webjars"),
				
				//The features are described by XML files found in these locations, one via Maven, one local
				editConfigurationFileExtend(FeaturesCfg.REPOSITORIES, 
						",mvn:org.ops4j.pax.wicket/features/2.1.0/xml/features" + 
						",file:" + featuresXmlPath), debugConfiguration("5005", false));
	}

	@Test
	public void testResourceCanBeFound() {
		
		//Register the URL handler
		Vfs.addDefaultURLTypes(new BundleUrlType());
		
		//Now try to grab the resource
		WebjarsResourceFinder finder = WebjarsResourceFinder.instance();
		assertThat(finder.find(WebjarsJavaScriptResourceReference.class, "/webjars/jquerypp/1.0b2/amd/jquerypp/range.js"), notNullValue());
	}

	
	//This allows us to spin up the container and access it through the CLI.
	public static void main(String[] args) throws Exception {
		OSGIContainerTest sampleTest = new OSGIContainerTest();
		ExamSystem system = PaxExamRuntime.createServerSystem(sampleTest.config());
		TestContainer container = PaxExamRuntime.createContainer(system);
		container.start();
	}
}