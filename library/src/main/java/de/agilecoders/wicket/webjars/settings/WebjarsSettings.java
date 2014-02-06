package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.WebJarAssetLocator;
import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.FileAssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.JarAssetPathCollector;
import org.apache.wicket.util.lang.Args;

import java.util.regex.Pattern;

/**
 * default {@link de.agilecoders.wicket.webjars.settings.IWebjarsSettings} implementation.
 *
 * @author miha
 */
public class WebjarsSettings implements IWebjarsSettings {

    private AssetPathCollector[] assetPathCollectors;
    private ResourceStreamProvider resourceStreamProvider = null;
    private String webjarsPackage;
    private String webjarsPath;
    private Pattern resourcePattern;
    private Pattern webjarsPathPattern;

    /**
     * Construct.
     */
    public WebjarsSettings() {
        this.resourceStreamProvider = ResourceStreamProvider.bestFitting();
        this.webjarsPackage = "META-INF.resources.webjars";
        this.webjarsPath = this.webjarsPackage.replaceAll("\\.", "/");
        this.resourcePattern = Pattern.compile(".*");
        this.webjarsPathPattern = Pattern.compile("/webjars/([^/]*)/([^/]*)/(.*)");

        this.assetPathCollectors = new AssetPathCollector[] {
                new FileAssetPathCollector(webjarsPath),
                new JarAssetPathCollector()
        };
    }

    @Override
    public ResourceStreamProvider resourceStreamProvider() {
        return resourceStreamProvider;
    }

    @Override
    public AssetPathCollector[] assetPathCollectors() {
        return assetPathCollectors;
    }

    @Override
    public String webjarsPackage() {
        return webjarsPackage;
    }

    @Override
    public String webjarsPath() {
        return webjarsPath;
    }

    @Override
    public ClassLoader[] classLoaders() {
        return new ClassLoader[] {
                Thread.currentThread().getContextClassLoader(),
                WebJarAssetLocator.class.getClassLoader(),
                getClass().getClassLoader()
        };
    }

    @Override
    public Pattern resourcePattern() {
        return resourcePattern;
    }

    @Override
    public Pattern webjarsPathPattern() {
        return webjarsPathPattern;
    }

    public WebjarsSettings resourcePattern(Pattern resourcePattern) {
        this.resourcePattern = resourcePattern;
        return this;
    }

    public WebjarsSettings webjarsPath(String webjarsPath) {
        this.webjarsPath = Args.notEmpty(webjarsPath, "webjarsPath");
        return this;
    }

    public WebjarsSettings webjarsPackage(String webjarsPackage) {
        this.webjarsPackage = Args.notEmpty(webjarsPackage, "webjarsPackage");
        return this;
    }

    public WebjarsSettings resourceStreamProvider(ResourceStreamProvider resourceStreamProvider) {
        this.resourceStreamProvider = Args.notNull(resourceStreamProvider, "resourceStreamProvider");
        return this;
    }

    public WebjarsSettings assetPathCollectors(AssetPathCollector... assetPathCollectors) {
        this.assetPathCollectors = Args.notNull(assetPathCollectors, "assetPathCollectors");
        return this;
    }
}