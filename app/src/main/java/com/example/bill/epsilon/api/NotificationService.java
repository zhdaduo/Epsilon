package com.example.bill.epsilon.api;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.notification.Notification;
import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import java.util.List;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface NotificationService {
  /**
   * 获取当前用户的通知列表
   */
  @GET("notifications.json")
  Observable<List<Notification>> readNotifications(
      @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  /**
   * 删除当前用户的某个通知
   */
  @DELETE("notifications/{id}.json") Observable<Ok> deleteNotification(
      @Path("id") int id);

  /**
   * 删除当前用户的所有通知
   */
  @DELETE("notifications/all.json") Observable<Ok> deleteAllNotifications();

  /**
   * 将当前用户的一些通知设成已读状态
   */
  @POST("notifications/read.json") Observable<Ok> markNotificationsAsRead(@Query("ids") int[] ids);

  /**
   * 获得未读通知数量
   */
  @GET("notifications/unread_count.json") Observable<NotificationsUnreadCount> unReadNotificationCount();
}
