package com.edulify.modules.sitemap;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.redfin.sitemapgenerator.WebSitemapGenerator;

import play.test.FakeApplication;
import play.test.Helpers;

public class AnnotationUrlProviderTest {

  private static final File baseDir = new File("/tmp");
  private static FakeApplication application;

  @BeforeClass
  public static void startApp() {
    application = Helpers.fakeApplication();
    Helpers.start(application);
  }

  @Test
  public void should_generate_sitemap_file() throws Exception {
    WebSitemapGenerator generator  = new WebSitemapGenerator("http://localhost:9000", baseDir);
    AnnotationUrlProvider provider = application.injector().instanceOf(AnnotationUrlProvider.class);
    provider.addUrlsTo(generator);
    generator.write();
    Assertions.assertThat(new File(baseDir, "sitemap.xml")).exists();
  }

  @Test
  public void should_generate_sitemap_with_url_for_annotated_action() throws Exception {
    WebSitemapGenerator generator  = new WebSitemapGenerator("http://localhost:9000", baseDir);
    AnnotationUrlProvider provider = application.injector().instanceOf(AnnotationUrlProvider.class);
    provider.addUrlsTo(generator);
    generator.write();

    String content = FileUtils.readFileToString(new File(baseDir, "sitemap.xml"));
    Assertions.assertThat(content).contains("<loc>http://localhost:9000/index</loc>");
    Assertions.assertThat(content).contains("<changefreq>monthly</changefreq>");
    Assertions.assertThat(content).contains("<priority>0.8</priority>");
  }

  @AfterClass
  public static void stopApp() {
    Helpers.stop(application);
    new File(baseDir, "sitemap.xml").delete();
  }
}