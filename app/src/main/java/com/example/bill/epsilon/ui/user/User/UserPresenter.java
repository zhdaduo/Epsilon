package com.example.bill.epsilon.ui.user.User;

import android.support.annotation.NonNull;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.event.UnFavoriteTopicEvent;
import com.example.bill.epsilon.bean.event.UnFollowUserEvent;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.user.User.UserMVP.Model;
import com.example.bill.epsilon.ui.user.User.UserMVP.View;
import com.example.bill.epsilon.util.RxUtil;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/18.
 */
@PerActivity
public class UserPresenter implements UserMVP.Presenter {

  private UserMVP.Model model;
  private UserMVP.View view;
  private RxErrorHandler mErrorHandler;

  @Inject
  public UserPresenter(Model model,
      View view, RxErrorHandler handler) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onUnFollowUser(UnFollowUserEvent event) {
    view.onFreshUserTopic();
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onUnFavoriteTopic(UnFavoriteTopicEvent event) {
    view.onFreshUserTopic();
  }

  public void getUser(String username) {
        model.getUserInfo(username)
            .compose(RxUtil.<UserDetailInfo>shortSchedulers())
            .compose(RxUtil.<UserDetailInfo>bindToLifecycle(view))
            .subscribe(new ErrorHandleSubscriber<UserDetailInfo>(mErrorHandler) {
              @Override
              public void onNext(@NonNull UserDetailInfo data) {
                view.onGetUserInfo(data);
              }
            });
  }

  public void followUser(String username) {
        model.followUser(username)
            .compose(RxUtil.<Ok>shortSchedulers())
            .compose(RxUtil.<Ok>bindToLifecycle(view))
            .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                ToastUtils.showShort("关注失败");
              }

              @Override
              public void onNext(@NonNull Ok data) {
                view.onFollowUser();
              }
            });
  }

  public void unfollowUser(String username) {
    model.unfollowUser(username)
        .compose(RxUtil.<Ok>shortSchedulers())
        .compose(RxUtil.<Ok>bindToLifecycle(view))
        .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("取消关注失败");
            ToastUtils.showShort(e.getMessage());
          }

          @Override
          public void onNext(@NonNull Ok ok) {
            view.onUnFollowUser();
          }
        });
  }

  @Override
  public void onDestroy() {
    view = null;
    mErrorHandler = null;
  }

  @Override
  public void onStart() {
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    EventBus.getDefault().unregister(this);
  }
}
