package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Article extends Model {
  @Id
  public long id;
  public Date createdAt;

  public String title;
  public String content;

  public static final Finder<Long, Article> find = new Finder<Long, Article>(
    Long.class, Article.class
  );

  @Override
  public void save() {
    this.createdAt = new Date();
    super.save();
  }
}