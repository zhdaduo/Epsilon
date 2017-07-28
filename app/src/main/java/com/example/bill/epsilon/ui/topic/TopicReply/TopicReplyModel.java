package com.example.bill.epsilon.ui.topic.TopicReply;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.TopicService;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class TopicReplyModel implements TopicReplyMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  TopicService service;

  @Inject
  public TopicReplyModel(CacheProviders cacheProviders) {
    this.cacheProviders = cacheProviders;
  }


  @Override
  public Observable<List<TopicReply>> getTopicReplies(int id, int offset, boolean update) {
    Observable<List<TopicReply>> reply = service.getReplies(id, offset, Constant.PAGE_SIZE);

    return cacheProviders.getReplies(reply, new DynamicKeyGroup(id, offset), new EvictDynamicKey(update))
        .flatMap(new Func1<Reply<List<TopicReply>>, Observable<List<TopicReply>>>() {
          @Override
          public Observable<List<TopicReply>> call(Reply<List<TopicReply>> listReply) {
            return Observable.just(listReply.getData());
          }
        });
  }
}
