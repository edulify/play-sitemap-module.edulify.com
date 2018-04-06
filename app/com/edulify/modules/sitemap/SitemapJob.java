package com.edulify.modules.sitemap;

import akka.actor.ActorSystem;
import play.Application;
import play.Play;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import akka.dispatch.MessageDispatcher;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SitemapJob {

    private SitemapConfig config;
    private SitemapTask task;
    private ActorSystem actorSystem;

    @Inject
    public SitemapJob(ActorSystem actorSystem, SitemapConfig config, SitemapTask task) {
        this.config = config;
        this.actorSystem = actorSystem;
        this.task = task;
        this.init();
    }

    private void init() {
        MessageDispatcher executionContext = actorSystem.dispatchers().lookup(config.getDispatcherName());

        this.actorSystem
                .scheduler()
                .schedule(
                        (FiniteDuration) FiniteDuration.create(config.getInitialDelay()),
                        (FiniteDuration) FiniteDuration.create(config.getExecutionInterval()),
                        task,
                        executionContext
                );
    }
}