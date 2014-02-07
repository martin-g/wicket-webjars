package de.agilecoders.wicket.webjars.settings;

import de.agilecoders.wicket.webjars.collectors.AssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.FileAssetPathCollector;
import de.agilecoders.wicket.webjars.collectors.JarAssetPathCollector;
import de.agilecoders.wicket.webjars.util.WebJarAssetLocator;
import org.apache.wicket.util.lang.Args;
import org.apache.wicket.util.time.Duration;

import java.util.regex.Pattern;

/**
 * default {@link de.agilecoders.wicket.webjars.settings.IWebjarsSettings} implementation.
 *
 * @author miha
 */
public class WebjarsSettings implements IWebjarsSettings {

    private Duration readFromCacheTimeout;
    private ResourceStreamProvider resourceStreamProvider;
    private String recentVersionPlaceHolder;
    private AssetPathCollector[] assetPathCollectors;
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
        this.recentVersionPlaceHolder = "current";
        this.readFromCacheTimeout = Duration.seconds(3);

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

    @Override
    public String recentVersionPlaceHolder() {
        return recentVersionPlaceHolder;
    }

    @Override
    public Duration readFromCacheTimeout() {
        return readFromCacheTimeout;
    }

    public WebjarsSettings readFromCacheTimeout(Duration readFromCacheTimeout) {
        this.readFromCacheTimeout = readFromCacheTimeout;
        return this;
    }

    public WebjarsSettings recentVersionPlaceHolder(String recentVersionPlaceHolder) {
        this.recentVersionPlaceHolder = recentVersionPlaceHolder;
        return this;
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