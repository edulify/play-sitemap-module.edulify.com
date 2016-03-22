package models;

import com.avaje.ebean.Model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Article extends Model {
  @Id
  public long id;
  public Date createdAt;

  public String title;
  public String content;

  public static final Finder<Long, Article> find = new Finder<>(Article.class);

  @Override
  public void save() {
    this.createdAt = new Date();
    super.save();
  }
}