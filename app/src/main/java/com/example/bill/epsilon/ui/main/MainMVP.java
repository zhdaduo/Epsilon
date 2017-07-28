package com.example.bill.epsilon.ui.main;

import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */

public interface MainMVP {

  interface View extends IView {

    void onLoginSuccess();

    void onLogoutSuccess();

    void onGetNotificationUnread(boolean hasUnread);
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {

    Observable<NotificationsUnreadCount> getUnreadCount();
  }
}
