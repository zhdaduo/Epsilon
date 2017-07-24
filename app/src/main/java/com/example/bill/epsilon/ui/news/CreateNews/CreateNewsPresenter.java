package com.example.bill.epsilon.ui.news.CreateNews;

import android.content.Context;
import android.support.annotation.NonNull;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.bean.event.CreateNewsEvent;
import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.bean.newsnode.NewsNode;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.news.CreateNews.CreateNewsMVP.Model;
import com.example.bill.epsilon.ui.news.CreateNews.CreateNewsMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.PrefUtil;
import com.google.gson.GsonBuilder;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class CreateNewsPresenter implements CreateNewsMVP.Presenter {

  private CompositeSubscription compositeSubscription;
  private CreateNewsMVP.Model model;
  private CreateNewsMVP.View view;
  private RxErrorHandler mErrorHandler;
  private Navigator navigator;
  private Context context;

  @Inject
  public CreateNewsPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator, Context context) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
    this.context = context;
    compositeSubscription = new CompositeSubscription();
  }

  public void createNews(String title, String link, int nodeId) {
    compositeSubscription.add(
    model.createNews(title, link, nodeId)
        .subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .doOnSubscribe(new Action0() {
          @Override
          public void call() {
            view.showLoading();
          }
        })
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .doAfterTerminate(new Action0() {
          @Override
          public void call() {
            view.hideLoading();
          }
        })
        .subscribe(new ErrorHandleSubscriber<News>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("创建失败");
          }

          @Override
          public void onNext(@NonNull News news) {
            ToastUtils.showShort("创建成功");
            EventBus.getDefault().post(new CreateNewsEvent());
            view.closeActivity();
          }
        })
    );
  }

  public void getNewsNodes() {
    compositeSubscription.add(
    model.getNewsNodes()
        .subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandleSubscriber<List<NewsNode>>(mErrorHandler) {
          @Override
          public void onNext(@NonNull List<NewsNode> data) {
            view.onGetNodes(data);
            // 缓存
            GsonBuilder builder = new GsonBuilder();
            PrefUtil.getInstance(context, Constant.Token.SHARED_PREFERENCES_NAME)
                .putString("news_nodes", builder.create().toJson(data));
          }
        })
    );
  }

  @Override
  public void onDestroy() {
    view = null;
    compositeSubscription.clear();
    mErrorHandler = null;
  }
}
