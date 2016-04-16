package com.edulify.modules.sitemap;

import play.Configuration;
import play.Environment;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.concurrent.TimeUnit;

@Singleton
public class SitemapConfig {

    private Configuration configuration;
    private Environment environment;


    @Inject
    public SitemapConfig(Configuration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
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

    public File getBaseDir() {
        String baseDir = configuration.getString("sitemap.baseDir");
        return baseDir == null ? environment.getFile("public") : new File(baseDir);
    }
}
