package com.example.bill.epsilon.ui.site;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.SiteService;
import com.example.bill.epsilon.bean.site.Site;
import com.example.bill.epsilon.bean.site.Site.Sites;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
public class SiteModel implements SiteMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  SiteService service;

  @Inject
  public SiteModel(CacheProviders cacheProviders) {
    this.cacheProviders = cacheProviders;
  }

  @Override
  public Observable<List<Site>> getSite(int offset, boolean update) {
    Observable<List<Site>> site = service.getSite();
    return cacheProviders.getSite(site, new DynamicKey(offset), new EvictDynamicKey(update))
        .flatMap(new Func1<Reply<List<Site>>, Observable<List<Site>>>() {
          @Override
          public Observable<List<Site>> call(Reply<List<Site>> listReply) {
            return Observable.just(listReply.getData());
          }
        });
  }
}
