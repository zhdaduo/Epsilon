package com.example.bill.epsilon.bean.newsnode;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bill on 2017/7/16.
 */

public class NewsNode {
  @SerializedName("id") private int id;
  @SerializedName("name") private String name;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
