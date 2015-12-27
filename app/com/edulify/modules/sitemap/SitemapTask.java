package com.edulify.modules.sitemap;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import play.Play;

import javax.inject.Inject;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class SitemapTask implements Runnable {

    private SitemapConfig sitemapConfig;
    private SitemapProviders sitemapProviders;

    @Inject
    public SitemapTask(SitemapConfig sitemapConfig, SitemapProviders providers) {
        this.sitemapConfig = sitemapConfig;
        this.sitemapProviders = providers;
    }

    @Override
    public void run() {
        String baseUrl = sitemapConfig.getBaseUrl();
        String baseDir = sitemapConfig.getBaseDir();
        if (baseDir == null) {
            // This should be removed in a next release and an Exception
            // will be thrown when baseDir is not configured.
            baseDir = Play.application().getFile("public").getAbsolutePath();
        }
        try {
            WebSitemapGenerator generator = new WebSitemapGenerator(baseUrl, new File(baseDir));
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
