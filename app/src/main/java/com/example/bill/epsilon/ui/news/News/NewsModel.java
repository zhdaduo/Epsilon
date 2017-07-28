package com.example.bill.epsilon.ui.news.News;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.NewsService;
import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.util.Constant;
import io.rx_cache.DynamicKeyGroup;
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
public class NewsModel implements NewsMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  NewsService service;

  @Inject
  public NewsModel(CacheProviders cacheProviders) {
   this.cacheProviders = cacheProviders;
  }

  @Override
  public Observable<List<News>> getNews(int offset, boolean update) {
    Observable<List<News>> news = service.readNews(null, offset, Constant.PAGE_SIZE);
    return cacheProviders.getNews(news, new DynamicKeyGroup(0, offset), new EvictDynamicKey(update))
        .flatMap(new Func1<Reply<List<News>>, Observable<List<News>>>() {
          @Override
          public Observable<List<News>> call(Reply<List<News>> listReply) {
            return Observable.just(listReply.getData());
          }
        });
  }
}
