package com.example.bill.epsilon.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import com.example.bill.epsilon.bean.event.GetUnreadCountEvent;
import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import com.example.bill.epsilon.bean.event.LoginEvent;
import com.example.bill.epsilon.bean.event.LogoutEvent;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.main.MainMVP.Model;
import com.example.bill.epsilon.ui.main.MainMVP.View;
import com.example.bill.epsilon.util.PrefUtil;
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
 * Created by Bill on 2017/7/19.
 */
@PerActivity
public class MainPresenter implements MainMVP.Presenter {

  private MainMVP.Model model;
  private MainMVP.View view;
  private RxErrorHandler mErrorHandler;
  private Context context;

  @Inject
  public MainPresenter(Model model,
      View view, RxErrorHandler handler, Context context) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.context = context;
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onLogoutSuccess(LogoutEvent event) {
    PrefUtil.clearMe(context);
    view.onLogoutSuccess();
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onLoginSuccess(LoginEvent event) {
    view.onLoginSuccess();
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onGetUnread(GetUnreadCountEvent event) {
    view.onGetNotificationUnread(event.hasUnread);
  }

  public void getUnreadCount() {
        model.getUnreadCount()
            .compose(RxUtil.<NotificationsUnreadCount>shortSchedulers())
            .compose(RxUtil.<NotificationsUnreadCount>bindToLifecycle(view))
            .subscribe(new ErrorHandleSubscriber<NotificationsUnreadCount>(mErrorHandler) {
              @Override
              public void onNext(@NonNull NotificationsUnreadCount data) {
                EventBus.getDefault().post(new GetUnreadCountEvent(data.getCount() > 0 ? true : false));
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
