package com.edulify.modules.sitemap;

import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class SitemapConfig {

    private Configuration configuration;


    @Inject
    public SitemapConfig(Configuration configuration) {
        this.configuration = configuration;
    }

    public Long getInitialDelayInMillis() {
        return configuration.getMilliseconds("sitemap.initialDelay", TimeUnit.MINUTES.toMillis(1));
    }

    public Long getExecutionIntervalInMillis() {
        return configuration.getMilliseconds("sitemap.executionInterval", TimeUnit.HOURS.toMillis(1));
    }

    public String getDispatcherName() {
        return configuration.getString("sitemap.dispatcher.name");
    }

    public String getInitialDelay() {
        return configuration.getString("sitemap.initialDelay", "1 minute");
    }

    public String getExecutionInterval() {
        return configuration.getString("sitemap.executionInterval", "1 hour");
    }

    public String getBaseUrl() {
        return configuration.getString("sitemap.baseUrl");
    }

    public String getBaseDir() {
        return configuration.getString("sitemap.baseDir");
    }
}
