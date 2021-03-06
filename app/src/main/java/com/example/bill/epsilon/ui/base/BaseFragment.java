package com.example.bill.epsilon.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Created by Bill on 2017/7/17.
 */

public abstract class BaseFragment extends RxFragment {

  protected boolean isViewInitiated;
  protected boolean isVisibleToUser;
  protected boolean isDataInitiated;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    isViewInitiated = true;
    prepareFetchData();
  }

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    this.isVisibleToUser = isVisibleToUser;
    prepareFetchData();
  }

  public abstract void fetchData();

  public boolean prepareFetchData() {
    return prepareFetchData(false);
  }

  public boolean prepareFetchData(boolean forceUpdate) {
    if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
      fetchData();
      isDataInitiated = true;
      return true;
    }
    return false;
  }
}
