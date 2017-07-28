package com.example.bill.epsilon.ui.topic.UserTopic;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.TopicService;
import com.example.bill.epsilon.api.server.UserService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.util.Constant;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import io.rx_cache.internal.RxCache;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/18.
 */
@PerFragment
public class UserTopicModel implements UserTopicMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  TopicService topicService;
  @Inject
  UserService userService;

  @Inject
  public UserTopicModel(CacheProviders cacheProviders) {
    this.cacheProviders = cacheProviders;
  }

  @Override
  public Observable<List<Topic>> getUserTopics(String username, int offset) {
    Observable<List<Topic>> topicCreate = userService.getUserCreateTopics(username, offset, Constant.PAGE_SIZE);
    return cacheProviders.getUserCreateTopics(topicCreate,  new DynamicKey(offset), new EvictDynamicKey(true))
        .flatMap(new Func1<Reply<List<Topic>>, Observable<List<Topic>>>() {
          @Override
          public Observable<List<Topic>> call(Reply<List<Topic>> listReply) {
            return Observable.just(listReply.getData());
          }
        });
  }

  @Override
  public Observable<List<Topic>> getUserFavorites(String username, int offset) {
    Observable<List<Topic>> userFavorite = userService.getUserFavoriteTopics(username, offset, Constant.PAGE_SIZE);
    return cacheProviders.getUserFavorites(userFavorite, new DynamicKey(offset), new EvictDynamicKey(true))
        .flatMap(new Func1<Reply<List<Topic>>, Observable<List<Topic>>>() {
          @Override
          public Observable<List<Topic>> call(Reply<List<Topic>> listReply) {
            return Observable.just(listReply.getData());
          }
        });
  }

  @Override
  public Observable<Ok> favoriteTopic(int id) {
    return topicService.favoriteTopic(id);
  }

  @Override
  public Observable<Ok> unfavoriteTopic(int id) {
    return topicService.unFavoriteTopic(id);
  }
}
