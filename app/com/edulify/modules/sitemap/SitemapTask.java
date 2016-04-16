package com.edulify.modules.sitemap;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SitemapTask implements Runnable {

    private SitemapConfig sitemapConfig;
    private SitemapProviders sitemapProviders;

    // Indicates the application is running, see #22 for more details
    private boolean running = true;

    @Inject
    public SitemapTask(SitemapConfig sitemapConfig, SitemapProviders providers, ApplicationLifecycle lifecycle) {
        this.sitemapConfig = sitemapConfig;
        this.sitemapProviders = providers;
        lifecycle.addStopHook(() -> {
            this.running = false;
            return CompletableFuture.completedFuture(null);
        });
    }

    @Override
    public void run() {
        // Akka triggers tasks also when it is shutting down
        if (!running) return;

        String baseUrl = sitemapConfig.getBaseUrl();
        try {
            WebSitemapGenerator generator = new WebSitemapGenerator(baseUrl, sitemapConfig.getBaseDir());
            List<UrlProvider> providers = sitemapProviders.getProviders();
            for (UrlProvider urlProvider : providers) {
                urlProvider.addUrlsTo(generator);
            }
            generator.write();
            try {
                generator.writeSitemapsWithIndex();
            } catch (RuntimeException ex) {
                play.Logger.warn("Could not create sitemap index", ex);
            }
        } catch(MalformedURLException ex) {
            play.Logger.error("Oops! Can't create a sitemap generator for the given baseUrl " + baseUrl, ex);
        }
    }
}
