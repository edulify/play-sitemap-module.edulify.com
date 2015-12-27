package com.edulify.modules.sitemap;

import com.google.inject.AbstractModule;

public class SitemapModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SitemapConfig.class).asEagerSingleton();
        bind(SitemapProviders.class).asEagerSingleton();
        bind(SitemapJob.class).asEagerSingleton();
    }
}
