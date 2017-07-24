package com.example.bill.epsilon;

import android.app.Application;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import com.blankj.utilcode.util.Utils;
import com.example.bill.epsilon.internal.di.component.ApplicationComponent;
import com.example.bill.epsilon.internal.di.component.DaggerApplicationComponent;
import com.example.bill.epsilon.internal.di.module.ApplicationModule;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.KeyStoreHelper;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created by Bill on 2017/7/16.
 */

public class AndroidApplication extends Application {

  private ApplicationComponent applicationComponent;
  private String BaseUrl = "https://www.diycode.cc/api/v3/";

  @Override
  public void onCreate() {
    super.onCreate();

    this.initializeInjector();

    Utils.init(getApplicationContext());

    try {
      KeyStoreHelper.createKeys(getApplicationContext(), Constant.KEYSTORE_KEY_ALIAS);
    } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }

    BGASwipeBackManager.getInstance().init(this);
  }

  private void initializeInjector() {
    this.applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this, BaseUrl))
        .build();
  }

  public ApplicationComponent getApplicationComponent() {
    return this.applicationComponent;
  }
}
