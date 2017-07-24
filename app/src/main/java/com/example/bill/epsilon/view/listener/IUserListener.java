package com.example.bill.epsilon.view.listener;

import android.view.View;
import com.example.bill.epsilon.bean.user.UserInfo;

/**
 * Created by Bill on 2017/7/19.
 */

public interface IUserListener {

  void onItemClick(View view, String username);

  void onItemLongClick(View view, UserInfo userInfo);
}
