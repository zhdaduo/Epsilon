package com.example.bill.epsilon.ui.notification;

import com.example.bill.epsilon.api.cache.CacheProviders;
import com.example.bill.epsilon.api.server.NotificationService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.notification.Notification;
import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class NotificationModel implements NotificationMVP.Model {

  private final CacheProviders cacheProviders;

  @Inject
  NotificationService service;

  @Inject
  public NotificationModel(CacheProviders cacheProviders) {
    this.cacheProviders = cacheProviders;
  }

  @Override
  public Observable<List<Notification>> getNotifications(int offset, boolean update) {
    Observable<List<Notification>> notification = service.readNotifications(offset, Constant.PAGE_SIZE);
    return cacheProviders.getNotifications(notification, new DynamicKey(offset), new EvictDynamicKey(update))
        .flatMap(new Func1<Reply<List<Notification>>, Observable<List<Notification>>>() {
          @Override
          public Observable<List<Notification>> call(Reply<List<Notification>> listReply) {
            return Observable.just(listReply.getData());
          }
        });
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
