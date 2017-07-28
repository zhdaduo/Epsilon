package com.example.bill.epsilon.ui.site;

import android.support.annotation.NonNull;
import com.example.bill.epsilon.bean.site.Site;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.site.SiteMVP.Model;
import com.example.bill.epsilon.ui.site.SiteMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.view.adapter.SiteAdapter;
import com.example.bill.epsilon.view.listener.ISiteListener;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
public class SitePresenter implements SiteMVP.Presenter {

  private List<Site> mList = new ArrayList<>();
  private int offset = 0;
  private boolean isFirst = true;
  private CompositeSubscription compositeSubscription;
  private SiteMVP.Model model;
  private SiteMVP.View view;
  private RxErrorHandler mErrorHandler;
  private SiteAdapter adapter;
  private Navigator navigator;

  @Inject
  public SitePresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
    compositeSubscription = new CompositeSubscription();
  }

  public void initAdapter() {
    if (adapter == null) {
      adapter = new SiteAdapter(mList, new ISiteListener() {
        @Override
        public void onItemCallBack(android.view.View view, String url) {
          navigator.navigateToWebActivity(url);
        }
      });
      view.setAdapter(adapter);
    }
  }

  public void getSite(final boolean isRefresh) {
    if (isRefresh) {
      offset = 0;
    }
    boolean isEvictCache = isRefresh;
    if (isRefresh && isFirst) {
      isFirst = false;
      isEvictCache = false;
    }
    compositeSubscription.add(
        model.getSite(offset, isEvictCache)
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .doOnSubscribe(new Action0() {
              @Override
              public void call() {
                if (isRefresh) {
                  view.showLoading();
                }
              }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate(new Action0() {
              @Override
              public void call() {
                if (isRefresh) {
                  view.hideLoading();
                }
              }
            })
            .subscribe(new ErrorHandleSubscriber<List<Site>>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.onLoadMoreError();
              }

              @Override
              public void onNext(@NonNull List<Site> data) {
                view.onLoadMoreComplete();
                if (offset == 0) {
                  mList.clear();
                }
                mList.addAll(data);
                adapter.notifyDataSetChanged();
                offset = adapter.getItemCount();
                if (data.size() < Constant.PAGE_SIZE) {
                  view.onLoadMoreEnd();
                }
              }
            })
    );
  }

  @Override
  public void onDestroy() {
    compositeSubscription.clear();
    adapter = null;
    view = null;
    mList = null;
    mErrorHandler = null;
  }
}
