package com.example.bill.epsilon.bean.event;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bill on 2017/7/16.
 */

public class NotificationsUnreadCount {
  @SerializedName("count") private int count;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
