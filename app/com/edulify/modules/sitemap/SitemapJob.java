package com.edulify.modules.sitemap;

import java.io.File;
import java.net.MalformedURLException;
import java.lang.ClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import play.Configuration;
import play.Play;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import akka.dispatch.MessageDispatcher;

import com.redfin.sitemapgenerator.WebSitemapGenerator;

public class SitemapJob implements Runnable {

  @Override
  public void run() {
    String baseUrl = Play.application().configuration().getString("sitemap.baseUrl");
    String baseDir = Play.application().configuration().getString("sitemap.baseDir");
    if (baseDir == null) {
      baseDir = Play.application().getFile("public").getAbsolutePath();
    }
    try {
      WebSitemapGenerator generator = new WebSitemapGenerator(baseUrl, new File(baseDir));
      List<UrlProvider> providers = providers();
      for (UrlProvider urlProvider : providers) {
        urlProvider.addUrlsTo(generator);
      }
      generator.write();
      try {
        generator.writeSitemapsWithIndex();
      } catch (RuntimeException ex) {
        play.Logger.warn("Could not create sitemap index", ex);
      }
    } catch(MalformedURLException ex) {
      play.Logger.error("Oops! Can't create a sitemap generator for the given baseUrl " + baseUrl, ex);
    } catch (ClassNotFoundException ex) {
      play.Logger.error("Cannot load configured url provider class", ex);
    } catch (InstantiationException | IllegalAccessException ex) {
      play.Logger.error("Cannot instantiate url provider class. Does it have a public default constructor?", ex);
    }
  }

  private List<UrlProvider> providers() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    Configuration configuration = Play.application().configuration();

    List<UrlProvider> providers = new ArrayList<>();
    providers.add(new AnnotationUrlProvider(configuration));

    String allProvidersClasses = configuration.getString("sitemap.providers");

    if (allProvidersClasses != null) {
      String[] providerClasses = allProvidersClasses.split(",");
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      for (String provider : providerClasses) {
        if (!"".equals(provider)) {
          Class<?> clazz = classLoader.loadClass(provider);
          Object providerInstance = clazz.newInstance();
          providers.add((UrlProvider)providerInstance);
        }
      }
    }

    return providers;
  }

  public static void startSitemapGenerator() {
    String dispatcherName = Play.application().configuration().getString("sitemap.dispatcher.name");
    MessageDispatcher executionContext = Akka.system().dispatchers().lookup(dispatcherName);
    Akka.system()
        .scheduler()
        .schedule(
            FiniteDuration.create(1, TimeUnit.MINUTES),
            FiniteDuration.create(1, TimeUnit.HOURS),
            new SitemapJob(),
            executionContext
        );
  }
}