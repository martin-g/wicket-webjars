wicket-webjars
==============

Integration of webjars for Apache Wicket. 

Current build status: [![Build Status](https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-webjars/badge/icon)](https://buildhive.cloudbees.com/job/l0rdn1kk0n/job/wicket-webjars/)

**wicket-webjars** dependes on [mustache.java](https://github.com/webjars/webjars).

Documentation:

- [Webjars Documentation](http://www.webjars.org/documentation)
- [Available Webjars](http://www.webjars.org)

Add maven dependency:

```xml
<dependency>
  <groupId>de.agilecoders.wicket.webjars</groupId>
  <artifactId>wicket-webjars</artifactId>
  <version>0.1.0</version>
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

        Webjars.install(this);
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
      <version>0.1.0</version>
  </dependency>

  <dependency>
      <groupId>org.webjars</groupId>
      <artifactId>jquery</artifactId>
      <version>1.8.3</version>
  </dependency>
</dependencies>
```


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
