package com.example.bill.epsilon.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created by Bill on 2017/7/22.
 */

public abstract class BaseActivity extends AppCompatActivity
    implements BGASwipeBackHelper.Delegate  {

  protected BGASwipeBackHelper mSwipeBackHelper;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    initSwipeBackFinish();
    super.onCreate(savedInstanceState);
  }

  private void initSwipeBackFinish() {
    mSwipeBackHelper = new BGASwipeBackHelper(this, this);
  }

  @Override public boolean isSupportSwipeBack() {
    return true;
  }

  @Override public void onSwipeBackLayoutSlide(float slideOffset) {
  }

  @Override public void onSwipeBackLayoutCancel() {
  }

  @Override public void onSwipeBackLayoutExecuted() {
    mSwipeBackHelper.swipeBackward();
  }


}
