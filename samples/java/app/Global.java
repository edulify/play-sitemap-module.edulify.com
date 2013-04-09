import play.Application;
import play.GlobalSettings;

import com.edulify.modules.sitemap.SitemapJob;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Global extends GlobalSettings {

  @Override
  public void onStart(Application app) {
   SitemapJob.startSitemapGenerator();
  }
}