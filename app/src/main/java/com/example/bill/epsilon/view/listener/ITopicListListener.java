package com.example.bill.epsilon.view.listener;

import android.view.View;
import com.example.bill.epsilon.bean.topic.Topic;

/**
 * Created by Bill on 2017/7/16.
 */

public interface ITopicListListener {

  void onUserItemClick(String username);

  void onItemClick(View view, int id);

  void onItemLongClick(View view, Topic topic);
}
