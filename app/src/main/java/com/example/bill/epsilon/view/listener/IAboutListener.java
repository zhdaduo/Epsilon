package com.example.bill.epsilon.view.listener;

import android.view.View;

/**
 * Created by Bill on 2017/7/2.
 */

public interface IAboutListener {

  void CardListener(View view);

  void ContributorListener(View view, String url);

  void LicenseListener(View view, String url);

}
