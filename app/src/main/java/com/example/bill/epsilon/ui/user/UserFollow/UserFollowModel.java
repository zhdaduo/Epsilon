package com.example.bill.epsilon.ui.user.UserFollow;

import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.user.UserInfo;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */
@PerFragment
public class UserFollowModel implements UserFollowMVP.Model {

  @Inject
  @Named("UserService")
  UserService service;

  @Inject
  @Named("AuthUserService")
  UserService serviceAuth;

  @Inject
  public UserFollowModel() {
  }

  @Override
  public Observable<List<UserInfo>> getFollowings(String username, int offset) {
    return service.getUserFollowing(Constant.VALUE_TOKEN_PREFIX + Constant.VALUE_TOKEN, username, offset, Constant.PAGE_SIZE);
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
