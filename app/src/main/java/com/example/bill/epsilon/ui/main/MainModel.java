package com.example.bill.epsilon.ui.main;

import com.example.bill.epsilon.api.server.NotificationService;
import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
public class MainModel implements MainMVP.Model {

  @Inject
  NotificationService service;

  @Inject
  public MainModel() {
  }

  @Override
  public Observable<NotificationsUnreadCount> getUnreadCount() {
    return service.unReadNotificationCount();
  }
}
