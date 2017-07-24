package com.example.bill.epsilon.ui.notification;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.notification.Notification;
import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.NotificationAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */

public interface NotificationMVP {

  interface View extends IView {

    void setAdapter(NotificationAdapter adapter);

    void setEmpty(boolean isEmpty);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();

    void setSubtitle(String subtitle);
  }

  interface Presenter extends IPresenter<View> {}

  interface Model {

    Observable<List<Notification>> getNotifications(int offset);

    Observable<Ok> deleteNotification(int id);

    Observable<Ok> deleteAllNotifications();

    Observable<NotificationsUnreadCount> getUnreadCount();

    Observable<Ok> readNotification(int[] ids);
  }
}
