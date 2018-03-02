# Sitemap Generator Module

This is a module that generates [sitemaps](http://www.sitemaps.org/) for [Play Framework](http://www.playframework.org/) 2.3.x, 2.4.x and 2.5.x. It is build using [SitemapGen4j](https://github.com/dfabulich/sitemapgen4j/) to generate the sitemap files.

[![Build Status](https://travis-ci.org/edulify/play-sitemap-module.edulify.com.svg)](https://travis-ci.org/edulify/play-sitemap-module.edulify.com)

## Compatibility matrix

| Playframework version | Module version |
|:----------------------|:---------------|
| 2.5.x                 | 2.1.2          |
| 2.5.x                 | 2.1.1          |
| 2.5.x                 | 2.1.0          |
| 2.4.6                 | 2.0.0          |
| 2.4.x                 | 2.0.0-M1       |
| 2.3.x                 | 1.1.8          |
| 2.3.x                 | 1.1.7          |
| 2.3.x                 | 1.1.6          |
| 2.2.x                 | 1.1.5          |
| 2.1.x                 | 1.1.3          |

## Configuring

The first step is include the sitemap module in your dependencies list, in `build.sbt` or `Build.scala` file:

#### `build.sbt`

```scala
resolvers ++= Seq(
  Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
)

...

libraryDependencies ++= Seq(
  "com.edulify" %% "sitemap-module" % "2.1.2"
)
```

#### `Build.scala`

```scala
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "sitemap-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    "com.edulify" %% "sitemap-module" % "2.1.2"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    resolvers += Resolver.url("Edulify Repository", url("https://edulify.github.io/modules/releases/"))(Resolver.ivyStylePatterns)
  )

}

```

Don't forget to add the resolver to your list of resolvers.

Ok, now you need to configure the module in your `application.conf` (the configuration syntax is defined by [Typesafe Config](https://github.com/typesafehub/config)):

```
play.modules.enabled += "com.edulify.modules.sitemap.SitemapModule"

...

sitemap {
  baseDir   = "/path/where/sitemaps/file/will/be/saved"
  baseUrl   = "http://localhost:9000"
  providers = "sitemap.providers.ArticlesUrlProvider"
  initialDelay = "1 minute"
  executionInterval = "2 hours"
}
```

- `baseUrl`: the base URL for links provided in your sitemap.
- `baseDir`: as it says, the complete path to directory where you want your sitemap xml files be saved. If you don't provide this value, the sitemap files will be saved under your `public` directory.
- `providers`: a comma separated list of *provider classes* that will generate URLs for your sitemap (see below).
- `initialDelay`: timespan between start of the application and first generation of the sitemap (defaults to "1 minute")
- `executionInterval`: timespan between each successive generation of the sitemap (defaults to "1 hour")

You must also configure an execution context that will execute the sitemap job:

```
sitemap.dispatcher.name = "akka.actor.sitemap-generator"
akka {

  // see play's thread pools configuration for more details

  actor {

    sitemap-generator = {
      fork-join-executor {
        parallelism-factor = 2.0
        parallelism-max = 24
      }
    }

  }
}
```

- `dispatcher.name`: name of the dispatcher used in the Akka system.

A complete example of the configuration is presented bellow:

```
play.modules.enabled += "com.edulify.modules.sitemap.SitemapModule"

sitemap {
  dispatcher {
    name    = "akka.actor.Sitemapper"
  }
  baseUrl   = "http://localhost:9000"
  providers = "sitemap.providers.ArticlesUrlProvider"
  initialDelay = "1 minute"
  executionInterval = "2 hours"
}

akka {
  // see play's thread pools configuration for more details
  actor {
    Sitemapper = {
      fork-join-executor {
        parallelism-factor = 2.0
        parallelism-max = 24
      }
    }
  }
}
```

## Using

With everything configured, now its time to add some URLs to your sitemap(s). The simplest way is add the `@SitemapItem` annotation to your controllers methods which doesn't expect arguments:

```java
package controllers;

import com.edulify.modules.sitemap.SitemapItem;
import com.redfin.sitemapgenerator.ChangeFreq;

public class Articles extends Controller {

  @SitemapItem
  public Result listArticles() { ... }

  @SitemapItem(changefreq = ChangeFreq.MONTHLY, priority = 0.8)
  public Result specialArticles() { ... }
}
```

This will add the route for such method in the sitemap. The default values for the *changefreq* and *priority* attributes are **daily** and **0.5** respectively.

For methods that needs arguments, you must implement a URL provider that will add the generated URL to sitemap. Here is an example:

```java
package sitemap.providers;

import java.net.MalformedURLException;
import java.util.List;

import com.edulify.modules.sitemap.SitemapConfig;
import models.Article;

import controllers.routes;

import com.edulify.modules.sitemap.UrlProvider;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapGenerator;

import javax.inject.Inject;

public class ArticlesUrlProvider implements UrlProvider {

  private SitemapConfig config;

  @Inject
  public ArticlesUrlProvider(SitemapConfig config) {
      this.config = config;
  }

  @Override
  public void addUrlsTo(WebSitemapGenerator generator) {
    String baseUrl = config.getBaseUrl();

    List<Article> articles = Article.find.all();
    for(Article article : articles) {
      String articleUrl = routes.Application.showArticle(article.id).url();
      try {
        WebSitemapUrl url = new WebSitemapUrl.Options(String.format("%s%s", baseUrl, articleUrl))
                                             .changeFreq(ChangeFreq.DAILY)
                                             .lastMod(article.createdAt)
                                             .priority(0.5)
                                             .build();
        generator.addUrl(url);
      } catch(MalformedURLException ex) {
        play.Logger.error("The generated url is not supported:  " + articleUrl, ex);
      }
    }
  }

}
```

Remember that you'll need to add the absolute path of the added URLs.

In order to deliver your sitemaps files, you can use the `Sitemaps` controller provided by this module. For this, you can simply add the following route to your `routes` files:

```
GET     /sitemap$suffix<[^/]*>.xml   com.edulify.modules.sitemap.Sitemaps.sitemap(suffix: String)
```

Or you can write your own file deliver. Just remember that the `sitemap_index.xml`, when generated, links to sitemap#.xml on the defined *baseUrl*, i.e., the `sitemap_index.xml` will like look this:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">

    <sitemap>
      <loc>http://www.example.com/sitemap1.xml</loc>
    </sitemap>

    <sitemap>
      <loc>http://www.example.com/sitemap2.xml</loc>
    </sitemap>

</sitemapindex>
```

for a `baseUrl = http://www.example.com`.

## Entry point

Some search engines provide an interface to add the site's sitemap. If your site has no providers or you don't expect that the number of links reaches 50.000 (maximum number of links in each sitemap file), you can point such engines to your `sitemap.xml`. Otherwise, you must point to `sitemap_index.xml`, that will have the links the generated sitemaps.

If you are using the `Sitemaps` controller from the module, you can always use the `sitemap_index.xml` as the entry point for the search engines; when no `sitemap_index.xml` is found, the `sitemap.xml` is automatically delivered.

## JPA Gotchas

Since Play only binds EntityManager when handling requests, you will get errors if you just try to use EntityManager directly inside the `UrlProvider`. In fact, even if you try `@Transactional` annotation, which is tightly coupled with [play actions composition](http://www.playframework.com/documentation/2.4.x/JavaActionsComposition), you will get a error complaining that there is no EntityManager bounded to the thread.

When using JPA, the correct way to query database outside of action thread is "*wrapping the call in `JPA.withTransaction`*":

```java
@Override
public void addUrlsTo(WebSitemapGenerator generator) {

    String baseUrl = sitemapConfig.getBaseUrl();

    for (Article article : listArticles()) {
        String articleUrl = routes.Application.showArticle(article.id).url();

        try {
            WebSitemapUrl url = new WebSitemapUrl.Options(String.format(
                    "%s%s", baseUrl, articleUrl))
                    .changeFreq(ChangeFreq.DAILY)
                    .priority(0.5).build();
            generator.addUrl(url);
        } catch (MalformedURLException ex) {
            play.Logger.error("Invalid URL: " + articleUrl, ex);
        }
    }
}

private List<Article> listArticles() {
    return JPA.withTransaction(new F.Function0<List<Article>>() {
        @Override
        public List<Article> apply() throws Throwable {
            EntityManager em = JPA.em();
            TypedQuery<Article> query = em.createNamedQuery(Article.FIND_ALL, Article.class);
            return query.getResultList();
        }
    });
}
```

## License

Copyright 2014 Edulify.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
