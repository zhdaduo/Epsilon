package com.example.bill.epsilon.bean.event;

/**
 * Created by Bill on 2017/7/19.
 */

public class GetUnreadCountEvent {

  public boolean hasUnread;

  public GetUnreadCountEvent(boolean hasUnread) {
    this.hasUnread = hasUnread;
  }
}
