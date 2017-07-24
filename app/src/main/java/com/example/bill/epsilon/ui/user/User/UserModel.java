package com.example.bill.epsilon.ui.user.User;

import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;

/**
 * Created by Bill on 2017/7/18.
 */

@PerActivity
public class UserModel implements UserMVP.Model {

  @Inject
  @Named("UserService")
  UserService service;

  @Inject
  @Named("UserServiceAuth")
  UserService serviceAuth;

  @Inject
  public UserModel() {
  }

  @Override
  public Observable<UserDetailInfo> getUserInfo(String username) {
    return service.getUser(username);
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
