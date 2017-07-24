package com.example.bill.epsilon.ui.news.News;

import android.support.annotation.NonNull;
import com.example.bill.epsilon.bean.event.CreateNewsEvent;
import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.news.News.NewsMVP.Model;
import com.example.bill.epsilon.ui.news.News.NewsMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.view.adapter.NewsAdapter;
import com.example.bill.epsilon.view.listener.INewsListener;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
public class NewsPresenter implements NewsMVP.Presenter {

  private List<News> mList = new ArrayList<>();
  private int offset = 0;
  private CompositeSubscription compositeSubscription;
  private NewsMVP.Model model;
  private NewsMVP.View view;
  private RxErrorHandler mErrorHandler;
  private NewsAdapter adapter;
  private Navigator navigator;

  @Inject
  public NewsPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
    compositeSubscription = new CompositeSubscription();
  }

  public void initAdapter() {
    if (adapter == null) {
      adapter = new NewsAdapter(mList, new INewsListener() {
        @Override
        public void onItemClick(android.view.View view, String url) {
          navigator.navigateToWebActivity(url);
        }
      });
      view.setAdapter(adapter);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void RefreshNews(CreateNewsEvent event) {
    view.onCreateNewsRefresh();
  }

  public void getNews(final boolean isRefresh) {
    if (isRefresh) {
      offset = 0;
    }
    compositeSubscription.add(
        model.getNews(offset)
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
            .subscribe(new ErrorHandleSubscriber<List<News>>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.onLoadMoreError();
              }

              @Override
              public void onNext(@NonNull List<News> data) {
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

  @Override
  public void onStart() {
    EventBus.getDefault().register(this);
  }

  @Override
  public void onStop() {
    EventBus.getDefault().unregister(this);
  }
}
