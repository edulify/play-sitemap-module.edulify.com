package controllers;

import models.Article;

import play.data.Form;
import play.mvc.Result;
import play.mvc.Controller;
import static play.data.Form.form;

import controllers.routes;

import com.edulify.modules.sitemap.SitemapItem;
import com.redfin.sitemapgenerator.ChangeFreq;

public class Application extends Controller {

  @SitemapItem(changefreq = ChangeFreq.MONTHLY, priority = 0.8)
  public static Result index() {
    return ok(views.html.index.render(Article.find.all()));
  }

  public static Result showArticle(Long id) {
    return ok(views.html.article.render(Article.find.byId(id)));
  }

  public static Result addArticle() {
    Form<Article> form = form(Article.class).bindFromRequest();
    Article article = form.get();
    article.save();
    return found(routes.Application.index());
  }

}
