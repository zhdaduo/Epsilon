package com.example.bill.epsilon.ui.user.User;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.UserService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/18.
 */

@PerActivity
public class UserModel implements UserMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  @Named("UserService")
  UserService service;

  @Inject
  @Named("UserServiceAuth")
  UserService serviceAuth;

  @Inject
  public UserModel(CacheProviders cacheProviders) {
    this.cacheProviders =cacheProviders;
  }

  @Override
  public Observable<UserDetailInfo> getUserInfo(String username) {
    Observable<UserDetailInfo> userDetailInfo = service.getUser(username);
    return cacheProviders.getUserInfo(userDetailInfo, new DynamicKey(username), new EvictDynamicKey(true))
        .flatMap(new Func1<Reply<UserDetailInfo>, Observable<UserDetailInfo>>() {
          @Override
          public Observable<UserDetailInfo> call(Reply<UserDetailInfo> userDetailInfoReply) {
            return Observable.just(userDetailInfoReply.getData());
          }
        });
  }

  @Override
  public Observable<Ok> followUser(String username) {
    return serviceAuth.followUser(username);
  }

  @Override
  public Observable<Ok> unfollowUser(String username) {
    return serviceAuth.unFollowUser(username);
  }
}
