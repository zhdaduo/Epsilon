package com.example.bill.epsilon.ui.topic.CreateReply;

import android.support.annotation.NonNull;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.event.ReplyEvent;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.topic.CreateReply.CreateReplyMVP.Model;
import com.example.bill.epsilon.ui.topic.CreateReply.CreateReplyMVP.View;
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
public class CreateReplyPresenter implements CreateReplyMVP.Presenter {

  private CompositeSubscription compositeSubscription;
  private CreateReplyMVP.Model model;
  private CreateReplyMVP.View view;
  private RxErrorHandler mErrorHandler;

  @Inject
  public CreateReplyPresenter(Model model,
      View view, RxErrorHandler handler) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    compositeSubscription = new CompositeSubscription();
  }

  public void createTopicReply(int topicId, String body) {
    compositeSubscription.add(
        model.createTopicReply(topicId, body)
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
            .subscribe(new ErrorHandleSubscriber<TopicReply>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                ToastUtils.showShort(R.string.reply_failed);
              }

              @Override
              public void onNext(@NonNull TopicReply reply) {
                ToastUtils.showShort(R.string.reply_success);
                EventBus.getDefault().post(new ReplyEvent());
                view.finishActivity();
              }
            })
    );
  }

  public void getTopicReply(int id) {
    compositeSubscription.add(
        model.getTopicReply(id)
        .subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ErrorHandleSubscriber<TopicReply>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
          }

          @Override
          public void onNext(@NonNull TopicReply reply) {
            view.onGetReply(reply.getBodyHtml());
          }
        })
    );
  }

  public void updateTopicReply(int id, String body) {
    compositeSubscription.add(
        model.updateTopicReply(id, body)
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
        .subscribe(new ErrorHandleSubscriber<TopicReply>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort(R.string.reply_failed);
          }

          @Override
          public void onNext(@NonNull TopicReply reply) {
            ToastUtils.showShort(R.string.reply_success);
            EventBus.getDefault().post(new ReplyEvent());
            view.finishActivity();
          }
        })
    );
  }

  public void deleteTopicReply(int id) {
    compositeSubscription.add(
        model.deleteTopicReply(id)
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
        .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort(R.string.delete_failed);
          }

          @Override
          public void onNext(@NonNull Ok ok) {
            ToastUtils.showShort(R.string.delete_success);
            EventBus.getDefault().post(new ReplyEvent());
            view.finishActivity();
          }
        })
    );
  }

  @Override
  public void onDestroy() {
    compositeSubscription.clear();
    mErrorHandler = null;
    view = null;
  }
}
