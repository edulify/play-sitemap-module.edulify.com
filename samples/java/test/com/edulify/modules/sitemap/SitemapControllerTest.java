package com.edulify.modules.sitemap;

import org.fest.assertions.Assertions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.edulify.modules.sitemap.routes.ref;

import play.api.mvc.HandlerRef;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;
import play.test.Helpers;

public class SitemapControllerTest {

  private static FakeApplication application;

  @BeforeClass
  public static void startApp() {
    application = Helpers.fakeApplication();
    Helpers.start(application);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_not_found_if_sitemap_file_does_not_exists() {
    FakeRequest request = Helpers.fakeRequest();
    HandlerRef action = ref.SitemapController.sitemap("-fake");
    Result result = Helpers.callAction(action, request);
    Assertions.assertThat(Helpers.status(result)).isEqualTo(Helpers.NOT_FOUND);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_sitemap_file_when_it_does_exists() {
    FakeRequest request = Helpers.fakeRequest();
    HandlerRef action = ref.SitemapController.sitemap("-blog");
    Result result = Helpers.callAction(action, request);
    Assertions.assertThat(Helpers.status(result)).isEqualTo(Helpers.OK);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_handle_malicious_directory_transversing_attack() {
    FakeRequest request = Helpers.fakeRequest();
    HandlerRef action = ref.SitemapController.sitemap("../conf/application.conf");
    Result result = Helpers.callAction(action, request);
    Assertions.assertThat(Helpers.status(result)).isEqualTo(Helpers.NOT_FOUND);
  }

  @Test @SuppressWarnings("rawtypes")
  public void should_return_not_found_when_given_path_is_a_directory() {
    FakeRequest request = Helpers.fakeRequest();
    HandlerRef action = ref.SitemapController.sitemap("javascripts");
    Result result = Helpers.callAction(action, request);
    Assertions.assertThat(Helpers.status(result)).isEqualTo(Helpers.NOT_FOUND);
  }

  @AfterClass
  public static void stopApp() {
    Helpers.stop(application);
  }
}