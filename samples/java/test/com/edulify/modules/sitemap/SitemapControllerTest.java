package com.edulify.modules.sitemap;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;

import play.Application;
import play.test.Helpers;

public class SitemapControllerTest {

  private static Application application;

  @BeforeClass
  public static void startApp() {
    application = Helpers.fakeApplication();
    Helpers.start(application);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_not_found_if_sitemap_file_does_not_exists() {
    Call action = routes.Sitemaps.sitemap("-fake");
    Http.RequestBuilder request = Helpers.fakeRequest(action);
    Result result = Helpers.route(request);
    Assertions.assertThat(result.status()).isEqualTo(Helpers.NOT_FOUND);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_sitemap_file_when_it_does_exists() {
    Call action = routes.Sitemaps.sitemap("-blog");
    Http.RequestBuilder request = Helpers.fakeRequest(action);
    Result result = Helpers.route(request);
    Assertions.assertThat(result.status()).isEqualTo(Helpers.OK);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_sitemap_file_root() {
    Call action = routes.Sitemaps.sitemap("");
    Http.RequestBuilder request = Helpers.fakeRequest(action);
    Result result = Helpers.route(request);
    Assertions.assertThat(result.status()).isEqualTo(Helpers.OK);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_sitemap_file_index() {
    Call action = routes.Sitemaps.sitemap("_index");
    Http.RequestBuilder request = Helpers.fakeRequest(action);
    Result result = Helpers.route(request);
    Assertions.assertThat(result.status()).isEqualTo(Helpers.OK);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_handle_malicious_directory_transversing_attack() {
    Call action = routes.Sitemaps.sitemap("../conf/application.conf");
    Http.RequestBuilder request = Helpers.fakeRequest(action);
    Result result = Helpers.route(request);
    Assertions.assertThat(result.status()).isEqualTo(Helpers.NOT_FOUND);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_not_found_when_given_path_is_a_directory() {
    Call action = routes.Sitemaps.sitemap("javascripts");
    Http.RequestBuilder request = Helpers.fakeRequest(action);
    Result result = Helpers.route(request);
    Assertions.assertThat(result.status()).isEqualTo(Helpers.NOT_FOUND);
  }

  @AfterClass
  public static void stopApp() {
    Helpers.stop(application);
  }
}