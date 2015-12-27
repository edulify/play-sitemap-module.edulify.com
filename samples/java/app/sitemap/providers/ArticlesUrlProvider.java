package sitemap.providers;

import java.net.MalformedURLException;
import java.util.List;

import com.edulify.modules.sitemap.SitemapConfig;
import models.Article;

import controllers.routes;

import com.edulify.modules.sitemap.UrlProvider;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ArticlesUrlProvider implements UrlProvider {

  private SitemapConfig sitemapConfig;

  @Inject
  public ArticlesUrlProvider(SitemapConfig sitemapConfig) {
      this.sitemapConfig = sitemapConfig;
  }

  @Override
  public void addUrlsTo(WebSitemapGenerator generator) {
    String baseUrl = sitemapConfig.getBaseUrl();

    List<Article> articles = Article.find.all();
    for(Article article : articles) {
      String articleUrl = routes.Application.showArticle(article.id).url();
      try {
        WebSitemapUrl url = new WebSitemapUrl.Options(String.format("%s%s", baseUrl, articleUrl))
                                             .changeFreq(ChangeFreq.DAILY)
                                             .lastMod(article.createdAt)
                                             .priority(0.5)
                                             .build();
        generator.addUrl(url);
      } catch(MalformedURLException ex) {
        play.Logger.error("The generated url is not supported:  " + articleUrl, ex);
      }
    }
  }

}