package com.example.bill.epsilon.ui.user.UserFollow;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.UserService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.bean.user.UserInfo;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.util.Constant;
import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/19.
 */
@PerFragment
public class UserFollowModel implements UserFollowMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  @Named("UserService")
  UserService service;

  @Inject
  @Named("AuthUserService")
  UserService serviceAuth;

  @Inject
  public UserFollowModel(CacheProviders cacheProviders) {
    this.cacheProviders = cacheProviders;
  }

  @Override
  public Observable<List<UserInfo>> getFollowings(String username, int offset, boolean update) {
    Observable<List<UserInfo>> userinfo = service.getUserFollowing(Constant.VALUE_TOKEN_PREFIX + Constant.VALUE_TOKEN, username, offset, Constant.PAGE_SIZE);
    return cacheProviders.getUserFollowing(userinfo, new DynamicKeyGroup(username, offset), new EvictDynamicKey(update))
        .flatMap(new Func1<Reply<List<UserInfo>>, Observable<List<UserInfo>>>() {
          @Override
          public Observable<List<UserInfo>> call(Reply<List<UserInfo>> listReply) {
            return Observable.just(listReply.getData());
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
