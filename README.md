wicket-webjars
==============

Integration of webjars for Apache Wicket. 

Current build status: [![Build Status](https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-webjars/badge/icon)](https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-webjars/)

**wicket-webjars** dependes on [webjars](https://github.com/webjars/webjars).

Current release version:

* For Wicket 8.x use 2.x
* For Wicket 7.x use 0.5.x
* For Wicket 6.x use 0.4.x


Documentation:

- [Webjars Documentation](http://www.webjars.org/documentation)
- [Available Webjars](http://www.webjars.org)

Add maven dependency:

```xml
<dependency>
  <groupId>de.agilecoders.wicket.webjars</groupId>
  <artifactId>wicket-webjars</artifactId>
  <version>0.4.10</version>
</dependency>
```

Installation:

```java
    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        // install 3 default collector instances
        // (FileAssetPathCollector(WEBJARS_PATH_PREFIX), JarAssetPathCollector, VfsAssetPathCollector)
        // and a webjars resource finder.
        WebjarsSettings settings = new WebjarsSettings();

        WicketWebjars.install(this, settings);
    }
```

Usage
=====

Add a webjars resource reference (css,js) to your IHeaderResponse:

```java
public WebjarsComponent extends Panel {

  public WebjarsComponent(String id) {
      super(id);
  }

  @Override
  public void renderHead(IHeaderResponse response) {
    super.renderHead(response);

    response.render(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("jquery/1.8.3/jquery.js")));
  }
}
```

Add dependencies to your pom.xml:

```xml
<dependencies>
  <dependency>
      <groupId>de.agilecoders.wicket.webjars</groupId>
      <artifactId>wicket-webjars</artifactId>
  </dependency>

  <dependency>
      <groupId>org.webjars</groupId>
      <artifactId>jquery</artifactId>
      <version>1.8.3</version>
  </dependency>
</dependencies>
```

It is also possible to use a resource by adding it to your html markup directly:

```html
<img src="/webjars/jquery-ui/1.9.2/css/smoothness/images/ui-icons_cd0a0a_256x240.png"/>
```

**Note**: The above works only for Servlet 3 web containers that automatically map META-INF/resources/* as browsable resources! Embedded Jetty or containers without this feature need extra configuration to enable this feature! Explicit version is also required (``jquery-ui/current/...`` urls are not handled).

To use always recent version from your pom you have to replace the version in path with the string "current". When resource
name gets resolved this string will be replaced by recent available version in classpath. (this feature is available since 0.2.0)

```java
public WebjarsComponent extends Panel {

  public WebjarsComponent(String id) {
      super(id);
  }

  @Override
  public void renderHead(IHeaderResponse response) {
    super.renderHead(response);

    response.render(JavaScriptHeaderItem.forReference(new WebjarsJavaScriptResourceReference("jquery/current/jquery.js")));
  }
}
```

**Note**: you must specify in your path either an explicit version (e.g. ``1.8.3``) or the ``<recentVersionPlaceHolder>`` configured in ``IWebjarsSettings`` (``current`` for a default config).

Authors
-------

[![Ohloh profile for Michael Haitz](https://www.openhub.net/accounts/l0rdn1kk0n/widgets/account_detailed.gif)](https://www.openhub.net/accounts/l0rdn1kk0n?ref=Detailed)

[![Ohloh profile for Martin Grigorov](https://www.openhub.net/accounts/mgrigorov/widgets/account_detailed.gif)](https://www.openhub.net/accounts/mgrigorov?ref=Detailed)

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/l0rdn1kk0n/wicket-webjars/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

