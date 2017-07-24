package com.example.bill.epsilon.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.example.bill.epsilon.util.CacheDataUtils;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.view.widget.SettingRowView;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import java.text.MessageFormat;

/**
 * Created by Bill on 2017/7/23.
 */

public class SettingActivity extends BaseActivity {

  private Unbinder mUnbinder;

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.btn_clear_cache) SettingRowView mBtnClearCache;
  @BindView(R.id.btn_check_update) SettingRowView mBtnCheckUpdate;
  @BindView(R.id.btn_logout) LinearLayout mBtnLogout;

  @BindColor(R.color.color_4d4d4d) int color_4d4d4d;
  @BindColor(R.color.color_999999) int color_999999;

  public static Intent getCallingIntent(Context context) {
    Intent callingIntent = new Intent(context, SettingActivity.class);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);
    mUnbinder =  ButterKnife.bind(this);

    mToolbar.setTitle(R.string.setting);
    mToolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(mToolbar);

    setupAppCache();
    setupAppVersion();

    if (PrefUtil.getToken(this).getAccessToken() != null) {
      mBtnLogout.setVisibility(View.VISIBLE);
    }
  }

  private void setupAppCache() {
    mBtnClearCache.setSettingDescription(
        MessageFormat.format(getString(R.string.total_cache_description), CacheDataUtils.getTotalCacheSize(this)));
  }

  private void setupAppVersion() {
    mBtnCheckUpdate.setSettingDescription(
        MessageFormat.format(getString(R.string.current_version), AppUtils.getAppVersionName()));

  }

  @OnClick(R.id.btn_clear_cache)
  void clearCache() {
    CacheDataUtils.clearAllCache(this);
    setupAppCache();
  }

  @OnClick(R.id.btn_check_update)
  void checkUpdate() {
    PgyUpdateManager.register(this, "com.example.bill.epsilon.fileprovider", new UpdateManagerListener() {
      @Override
      public void onNoUpdateAvailable() {
        ToastUtils.showShort("您的应用为最新版本");
      }

      @Override
      public void onUpdateAvailable(String result) {
        final AppBean appBean = getAppBeanFromString(result);
        new MaterialDialog.Builder(SettingActivity.this)
            .title("发现新版本")
            .content(appBean.getReleaseNote())
            .contentColor(color_4d4d4d)
            .positiveText(R.string.update)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                startDownloadTask(SettingActivity.this, appBean.getDownloadURL());
              }
            })
            .negativeText(R.string.cancel)
            .negativeColor(color_999999)
            .show();
      }
    });
  }

  @OnClick(R.id.btn_feedback)
  void feedback() {
    Navigator navigator = ((AndroidApplication)getApplicationContext()).getApplicationComponent().getNavigator();
    navigator.navigateToBrower("https://github.com/zhdaduo/diycode/issues");
  }

  @OnClick(R.id.btn_logout)
  void clickLogout() {
    new MaterialDialog.Builder(this)
        .content("确定要退出登录吗？")
        .contentColor(color_4d4d4d)
        .positiveText(R.string.confirm)
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            Intent intent = new Intent(getResources().getString(R.string.logout_intent_action));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
          }
        })
        .negativeText(R.string.cancel)
        .negativeColor(color_999999)
        .show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
      mUnbinder.unbind();
    }
    this.mUnbinder = null;
  }
}
