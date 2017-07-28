package com.example.bill.epsilon.ui.user.UserFollow;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.event.FollowUserEvent;
import com.example.bill.epsilon.bean.event.UnFollowUserEvent;
import com.example.bill.epsilon.bean.user.UserInfo;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.user.UserFollow.UserFollowMVP.Model;
import com.example.bill.epsilon.ui.user.UserFollow.UserFollowMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.view.adapter.UserAdapter;
import com.example.bill.epsilon.view.listener.IUserListener;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/19.
 */
@PerFragment
public class UserFollowPresenter implements UserFollowMVP.Presenter {

  private List<UserInfo> mList = new ArrayList<>();
  private int offset = 0;
  private boolean isFirst = true;
  private CompositeSubscription compositeSubscription;
  private UserFollowMVP.Model model;
  private UserFollowMVP.View view;
  private RxErrorHandler mErrorHandler;
  private UserAdapter adapter;
  private Navigator navigator;

  @Inject
  public UserFollowPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
    compositeSubscription = new CompositeSubscription();
  }

  public void initAdapter(final String username) {
    if (adapter == null) {
      adapter = new UserAdapter(mList, new IUserListener() {
        @Override
        public void onItemClick(android.view.View view, String username) {
          navigator.navigateToUserActivity(username);
        }

        @Override
        public void onItemLongClick(final android.view.View view, final UserInfo userInfo) {

          if (!TextUtils.isEmpty(PrefUtil.getToken(view.getContext()).getAccessToken())) {
            if (PrefUtil.getMe(view.getContext()).getLogin().equals(username)) {
              new MaterialDialog.Builder(view.getContext()).items("取消关注")
                  .itemsCallback(new ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, android.view.View itemView,
                        int position, CharSequence text) {
                      unfollowUser(userInfo);
                    }
                  }).show();
            } else {
              new MaterialDialog.Builder(view.getContext()).items("关注")
                  .itemsCallback(new ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, android.view.View itemView,
                        int position, CharSequence text) {
                      followUser(userInfo);
                    }
                  }).show();
            }
          } else {
            ToastUtils.showShort(R.string.please_login);
          }
        }
      });
      view.setAdapter(adapter);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onFollowRefresh(FollowUserEvent event) {
    view.onFollowRefresh();
  }

  public void getUsers(String username, final boolean isRefresh) {
    if (isRefresh) {
      offset = 0;
    }
    boolean isEvictCache = isRefresh;
    if (isRefresh && isFirst) {
      isFirst = false;
      isEvictCache = false;
    }
    compositeSubscription.add(
        model.getFollowings(username, offset, isEvictCache)
            .retryWhen(new RetryWithDelay(3, 2))
            .doOnSubscribe(new Action0() {
              @Override
              public void call() {
                if (isRefresh) {
                  view.showLoading();
                }
              }
            }).subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate(new Action0() {
              @Override
              public void call() {
                if (isRefresh) {
                  view.hideLoading();
                }
              }
            })
            .subscribe(new ErrorHandleSubscriber<List<UserInfo>>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.onLoadMoreError();
              }

              @Override
              public void onNext(@NonNull List<UserInfo> data) {
                view.onLoadMoreComplete();
                if (offset == 0) {
                  mList.clear();
                }
                mList.addAll(data);
                adapter.notifyDataSetChanged();
                offset = adapter.getItemCount();
                if (data.size() < Constant.PAGE_SIZE) {
                  view.onLoadMoreEnd();
                }
                view.setEmpty(mList.isEmpty());
              }
            })
    );
  }

  public void followUser(final UserInfo user) {
    compositeSubscription.add(
        model.followUser(user.getLogin())
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                ToastUtils.showShort("关注失败");
              }

              @Override
              public void onNext(@NonNull Ok data) {
                EventBus.getDefault().post(new FollowUserEvent());
                //refresh userActivity
                EventBus.getDefault().post(new UnFollowUserEvent());
                ToastUtils.showShort("已关注");
              }
            })
    );
  }

  public void unfollowUser(final UserInfo user) {
    compositeSubscription.add(
        model.unfollowUser(user.getLogin())
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                ToastUtils.showShort("取消关注失败");
              }

              @Override
              public void onNext(@NonNull Ok ok) {
                ToastUtils.showShort("已取消关注");
                EventBus.getDefault().post(new UnFollowUserEvent());
                mList.remove(user);
                adapter.notifyDataSetChanged();
                view.setEmpty(mList.isEmpty());
              }
            })
    );
  }

  @Override
  public void onDestroy() {
    compositeSubscription.clear();
    adapter = null;
    view = null;
    mList = null;
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
