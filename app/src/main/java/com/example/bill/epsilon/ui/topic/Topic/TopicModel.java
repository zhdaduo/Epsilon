package com.example.bill.epsilon.ui.topic.Topic;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.TopicService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Like;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
public class TopicModel implements TopicMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  TopicService service;

  @Inject
  public TopicModel(CacheProviders cacheProviders) {
    this.cacheProviders = cacheProviders;
  }

  @Override
  public Observable<TopicDetail> getTopicDetail(int id) {
    Observable<TopicDetail> topicDetail = service.getTopic(id);
    return cacheProviders.getTopicDetail(topicDetail,  new DynamicKey(id), new EvictDynamicKey(true))
        .flatMap(new Func1<Reply<TopicDetail>, Observable<TopicDetail>>() {
          @Override
          public Observable<TopicDetail> call(Reply<TopicDetail> topicReply) {
            return Observable.just(topicReply.getData());
          }
        });
  }

  @Override
  public Observable<Like> likeTopic(String obj_type, Integer obj_id) {
    return service.like(obj_type, obj_id);
  }

  @Override
  public Observable<Like> unlikeTopic(String obj_type, Integer obj_id) {
    return service.unLike(obj_type, obj_id);
  }

  @Override
  public Observable<Ok> favoriteTopic(int id) {
    return service.favoriteTopic(id);
  }

  @Override
  public Observable<Ok> unfavoriteTopic(int id) {
    return service.unFavoriteTopic(id);
  }
}
