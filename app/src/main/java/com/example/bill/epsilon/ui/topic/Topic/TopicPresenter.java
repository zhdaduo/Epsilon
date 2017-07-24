package com.example.bill.epsilon.ui.topic.Topic;

import android.support.annotation.NonNull;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Like;
import com.example.bill.epsilon.bean.event.LoadTopicDetailFinishEvent;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.topic.Topic.TopicMVP.Model;
import com.example.bill.epsilon.ui.topic.Topic.TopicMVP.View;
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
 * Created by Bill on 2017/7/19.
 */
@PerActivity
public class TopicPresenter implements TopicMVP.Presenter {

  private static final String LIKE_OBJ_TYPE_TOPIC = "topic";
  private CompositeSubscription compositeSubscription;
  private TopicMVP.Model model;
  private TopicMVP.View view;
  private RxErrorHandler mErrorHandler;
  private Navigator navigator;

  @Inject
  public TopicPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
    compositeSubscription = new CompositeSubscription();
  }

  public void getTopicDetail(int id) {
    compositeSubscription.add(
        model.getTopicDetail(id)
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
            .subscribe(new ErrorHandleSubscriber<TopicDetail>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.setLayout(false);
              }

              @Override
              public void onNext(@NonNull TopicDetail data) {
                view.setLayout(true);
                view.onGetTopicDetail(data);
              }
            })
    );
  }

  public void favoriteTopic(int id) {
    compositeSubscription.add(
        model.favoriteTopic(id)
        .subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("收藏失败");
          }

          @Override
          public void onNext(@NonNull Ok data) {
            view.onFavoriteTopic();
          }
        })
    );
  }

  public void unfavoriteTopic(int id) {
    compositeSubscription.add(
        model.unfavoriteTopic(id)
        .subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("取消收藏失败");
          }

          @Override
          public void onNext(@NonNull Ok data) {
            view.onFavoriteTopic();
          }
        })
    );
  }

  public void likeTopic(Integer obj_id) {
    compositeSubscription.add(
        model.likeTopic(LIKE_OBJ_TYPE_TOPIC, obj_id)
        .subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandleSubscriber<Like>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("点赞失败");
          }

          @Override
          public void onNext(@NonNull Like data) {
            view.onLikeTopic(data);
          }
        })
    );
  }

  public void unlikeTopic(Integer obj_id) {
    compositeSubscription.add(
        model.unlikeTopic(LIKE_OBJ_TYPE_TOPIC, obj_id)
        .subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandleSubscriber<Like>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("取消点赞失败");
          }

          @Override
          public void onNext(@NonNull Like data) {
            view.onLikeTopic(data);
          }
        })
    );
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void loadTopicFinish(LoadTopicDetailFinishEvent event) {
    view.hideLoading();
  }

  @Override
  public void onDestroy() {
    compositeSubscription.clear();
    view = null;
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
