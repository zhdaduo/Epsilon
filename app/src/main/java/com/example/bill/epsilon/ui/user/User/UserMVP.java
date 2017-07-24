package com.example.bill.epsilon.ui.user.User;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import rx.Observable;

/**
 * Created by Bill on 2017/7/18.
 */

public interface UserMVP {

  interface View {

    void onGetUserInfo(UserDetailInfo user);

    void onFollowUser();

    void onUnFollowUser();

    void onFreshUserTopic();
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {

    Observable<UserDetailInfo> getUserInfo(String username);

    Observable<Ok> followUser(String username);

    Observable<Ok> unfollowUser(String username);
  }
}
