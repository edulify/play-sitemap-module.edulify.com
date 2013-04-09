package com.edulify.modules.sitemap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.redfin.sitemapgenerator.ChangeFreq;

/**
 * The annotation to declare actions as sitemap entries.
 * See more details at <a href="http://sitemaps.org">http://sitemaps.org</a>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SitemapItem {

  /**
   * How frequently the page is likely to change. This value provides
   * general information to search engines and may not correlate exactly
   * to how often they crawl the page.
   *
   * @return one of the following values: always, hourly, daily, weekly, monthly, yearly, never
   */
  ChangeFreq changefreq() default ChangeFreq.DAILY;

  /**
   * The priority of this URL relative to other URLs on your site.
   * Valid values range from 0.0 to 1.0. This value does not affect
   * how your pages are compared to pages on other sites—it only lets
   * the search engines know which pages you deem most important for
   * the crawlers.
   *
   * The default priority of a page is 0.5.
   *
   * @return
   */
  double priority() default 0.5;
}
