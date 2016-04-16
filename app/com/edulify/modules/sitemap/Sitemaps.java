package com.edulify.modules.sitemap;

import static java.lang.String.format;

import java.io.File;

import play.Configuration;
import play.Environment;
import play.mvc.Result;
import play.mvc.Controller;

import javax.inject.Inject;

public class Sitemaps extends Controller {

  private Configuration configuration;

  private Environment environment;

  @Inject
  public Sitemaps(Configuration configuration, Environment environment) {
    this.configuration = configuration;
    this.environment = environment;
  }

  public Result sitemap(String sitemapSuffix) {
    String sitemap = String.format("sitemap%s.xml", sitemapSuffix);
    File baseDir = baseDir();
    File sitemapFile = new File(baseDir, sitemap);
    play.Logger.debug("Delivering sitemap file " + sitemapFile.getAbsolutePath());
    if(canDelivery(sitemapFile)) {
      return ok(sitemapFile, true);
    }
    if ("_index".equals(sitemapSuffix)) {
      return sitemap("");
    }
    play.Logger.error(format("%s sitemap file was not found at directory %s", sitemapFile.getAbsolutePath(), baseDir.getAbsolutePath()));
    return notFound();
  }

  private File baseDir() {
    String baseDir = configuration.getString("sitemap.baseDir");
    return baseDir != null ? new File(baseDir) : environment.getFile("public");
  }

  private boolean canDelivery(File file) {
    File baseDir = baseDir();
    return  file.exists() &&
            file.isFile() &&
            file.getParentFile().equals(baseDir);
  }
}