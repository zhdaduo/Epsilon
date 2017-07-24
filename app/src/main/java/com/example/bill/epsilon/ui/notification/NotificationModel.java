package com.example.bill.epsilon.ui.notification;

import com.example.bill.epsilon.api.NotificationService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.notification.Notification;
import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class NotificationModel implements NotificationMVP.Model {

  @Inject
  NotificationService service;

  @Inject
  public NotificationModel() {
  }

  @Override
  public Observable<List<Notification>> getNotifications(int offset) {
    return service.readNotifications(offset, Constant.PAGE_SIZE);
  }

  @Override
  public Observable<Ok> deleteNotification(int id) {
    return service.deleteNotification(id);
  }

  @Override
  public Observable<Ok> deleteAllNotifications() {
    return service.deleteAllNotifications();
  }

  @Override
  public Observable<NotificationsUnreadCount> getUnreadCount() {
    return service.unReadNotificationCount();
  }

  @Override
  public Observable<Ok> readNotification(int[] ids) {
    return service.markNotificationsAsRead(ids);
  }
}
