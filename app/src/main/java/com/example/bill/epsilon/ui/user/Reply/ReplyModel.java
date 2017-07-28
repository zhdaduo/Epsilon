package com.example.bill.epsilon.ui.user.Reply;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.UserService;
import com.example.bill.epsilon.bean.topic.Reply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class ReplyModel implements ReplyMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  UserService service;

  @Inject
  public ReplyModel(CacheProviders cacheProviders) {
    this.cacheProviders = cacheProviders;
  }

  @Override
  public Observable<List<Reply>> getUserReplies(String username, int offset, boolean update) {
    Observable<List<Reply>> listObservable = service.getUserReplies(username, offset, Constant.PAGE_SIZE);
    return cacheProviders.getUserReplies(listObservable, new DynamicKeyGroup(username, offset), new EvictDynamicKey(update))
        .flatMap(new Func1<io.rx_cache.Reply<List<Reply>>, Observable<List<Reply>>>() {
          @Override
          public Observable<List<Reply>> call(io.rx_cache.Reply<List<Reply>> listReply) {
            return Observable.just(listReply.getData());
          }
        });
  }
}
