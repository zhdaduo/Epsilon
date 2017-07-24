package com.example.bill.epsilon.ui.topic.CreateTopic;

import android.content.Context;
import android.support.annotation.NonNull;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.bean.event.CreateTopicEvent;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.bean.topicnode.Node;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.topic.CreateTopic.CreateTopicMVP.Model;
import com.example.bill.epsilon.ui.topic.CreateTopic.CreateTopicMVP.View;
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
public class CreateTopicPresenter implements CreateTopicMVP.Presenter {

  private CompositeSubscription compositeSubscription;
  private CreateTopicMVP.Model model;
  private CreateTopicMVP.View view;
  private RxErrorHandler mErrorHandler;
  private Context context;

  @Inject
  public CreateTopicPresenter(Model model,
      View view, RxErrorHandler handler, Context context) {
    this.model = model;
    this.view = view;
    this.context = context;
    this.mErrorHandler = handler;
    compositeSubscription = new CompositeSubscription();
  }

  public void createTopic(String title, String body, int nodeId) {
    compositeSubscription.add(
    model.createTopic(title, body, nodeId)
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
            ToastUtils.showShort("发布失败");
          }

          @Override
          public void onNext(@NonNull TopicDetail topic) {
            ToastUtils.showShort("发布成功");
            EventBus.getDefault().post(new CreateTopicEvent());
            view.showNewTopic(topic);
            view.finishActivity();
          }
        })
    );
  }

  public void getNodes() {
    compositeSubscription.add(
        model.getNodes()
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ErrorHandleSubscriber<List<Node>>(mErrorHandler) {
              @Override
              public void onNext(@NonNull List<Node> data) {
                view.onGetNodes(data);
                // 缓存
                GsonBuilder builder = new GsonBuilder();
                PrefUtil.getInstance(context, Constant.Token.SHARED_PREFERENCES_NAME)
                    .putString("topic_nodes", builder.create().toJson(data));
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
