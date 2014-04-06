package com.edulify.modules.sitemap;

import com.redfin.sitemapgenerator.WebSitemapGenerator;

/**
 * A provider for dynamic urls.
 */
public interface UrlProvider {

  /**
   * Receives a sitemap generator and add urls to it.
   *
   * @param generator the web site generator
   */
  public void addUrlsTo(WebSitemapGenerator generator);
}