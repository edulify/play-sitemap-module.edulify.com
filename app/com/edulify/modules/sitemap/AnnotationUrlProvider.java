package com.edulify.modules.sitemap;

import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapGenerator;

import java.lang.ClassLoader;
import java.lang.InstantiationException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import play.Configuration;
import play.mvc.Call;

import javax.inject.Inject;

public class AnnotationUrlProvider implements UrlProvider {

  private Configuration configuration;

  @Inject
  public AnnotationUrlProvider(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void addUrlsTo(WebSitemapGenerator generator) {
    String baseUrl = configuration.getString("sitemap.baseUrl");

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Reflections reflections = new Reflections("controllers", new MethodAnnotationsScanner());

    Set<Method> actions = reflections.getMethodsAnnotatedWith(SitemapItem.class);
    for(Method method : actions) {
      String actionUrl = actionUrl(classLoader, method);
      SitemapItem annotation = method.getAnnotation(SitemapItem.class);
      if(annotation != null) {
        WebSitemapUrl url = webSitemapUrl(baseUrl, actionUrl, annotation);
        generator.addUrl(url);
      }
    }
  }

  private WebSitemapUrl webSitemapUrl(String baseUrl, String actionUrl, SitemapItem annotation) {
    try {
      return new WebSitemapUrl.Options(String.format("%s%s", baseUrl, actionUrl))
                              .changeFreq(annotation.changefreq())
                              .priority(annotation.priority())
                              .build();
    } catch(MalformedURLException ex) {
      play.Logger.error("MalformedURLException: ", ex);
    }
    return null;
  }

  private String actionUrl(ClassLoader classLoader, Method method) {
    String itemUrl = "";
    try {
      String className  = method.getDeclaringClass().getSimpleName();
      String methodName = method.getName();
      Class<?> clazz    = classLoader.loadClass(String.format("controllers.Reverse%s", className));
      Method reverseMethod = clazz.getMethod(methodName);
      Call call = (Call) reverseMethod.invoke(clazz.newInstance());
      itemUrl = call.url();
    } catch (ClassNotFoundException ex) {
      play.Logger.error("Package controllers does not have such class", ex);
    } catch (NoSuchMethodException ex) {
      play.Logger.error("Method not exists", ex);
    } catch (IllegalAccessException ex) {
      play.Logger.error("Method is not visible", ex);
    } catch (InstantiationException ex) {
      play.Logger.error("Reverse class could not be instantiated", ex);
    } catch (InvocationTargetException ex) {
      play.Logger.error("No instance of reverse class to call method", ex);
    }
    return itemUrl;
  }
}