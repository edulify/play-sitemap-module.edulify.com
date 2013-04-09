package sitemap.providers;

import java.net.MalformedURLException;
import java.util.List;

import models.Article;

import controllers.routes;
import play.Play;

import com.edulify.modules.sitemap.UrlProvider;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapGenerator;

public class ArticlesUrlProvider implements UrlProvider {

  @Override
  public void addUrlsTo(WebSitemapGenerator generator) {
    String baseUrl = Play.application().configuration().getString("sitemap.baseUrl");

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
        play.Logger.error("wat? " + articleUrl, ex);
      }
    }
  }

}