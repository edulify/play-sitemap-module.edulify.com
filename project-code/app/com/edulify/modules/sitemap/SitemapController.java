package com.edulify.modules.sitemap;

import static java.lang.String.format;

import java.io.File;

import play.Play;
import play.mvc.Result;
import play.mvc.Controller;

public class SitemapController extends Controller {

  public static Result sitemap(String sitemapSuffix) {
    String sitemap = String.format("sitemap%s.xml", sitemapSuffix);
    File baseDir = baseDir();
    File sitemapFile = new File(baseDir, sitemap);
    play.Logger.debug("Delivering sitemap file " + sitemapFile.getAbsolutePath());
    if(canDelivery(sitemapFile)) {
      return ok(sitemapFile);
    }
    if ("_index".equals(sitemapSuffix)) {
      return sitemap("");
    }
    play.Logger.error(format("%s sitemap file was not found at directory %s", sitemapFile.getAbsolutePath(), baseDir.getAbsolutePath()));
    return notFound();
  }

  private static File baseDir() {
    String baseDir = Play.application().configuration().getString("sitemap.baseDir");
    return baseDir != null ? new File(baseDir) : Play.application().getFile("public");
  }

  private static boolean canDelivery(File file) {
    File baseDir = baseDir();
    return  file.exists() &&
            file.isFile() &&
            file.getParentFile().equals(baseDir);
  }

}