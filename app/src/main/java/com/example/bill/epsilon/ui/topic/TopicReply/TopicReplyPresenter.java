package com.example.bill.epsilon.ui.topic.TopicReply;

import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.bean.event.ReplyEvent;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.topic.TopicReply.TopicReplyMVP.Model;
import com.example.bill.epsilon.ui.topic.TopicReply.TopicReplyMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.util.html.HtmlUtils.Callback;
import com.example.bill.epsilon.view.adapter.TopicReplyAdapter;
import com.example.bill.epsilon.view.adapter.TopicReplyAdapter.OnItemClickListener;
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
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class TopicReplyPresenter implements TopicReplyMVP.Presenter {

  private List<TopicReply> mList = new ArrayList<>();
  private CompositeSubscription compositeSubscription;
  private TopicReplyAdapter adapter;
  private TopicReplyMVP.Model model;
  private TopicReplyMVP.View view;
  private RxErrorHandler mErrorHandler;
  private Navigator navigator;
  private int offset = 0;
  private boolean isFirst = true;

  @Inject
  public TopicReplyPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
    compositeSubscription = new CompositeSubscription();
  }

  public void initAdapter(final String title) {
    if (adapter == null) {
      adapter = new TopicReplyAdapter(mList, new OnItemClickListener() {
        @Override
        public void onUserClick(android.view.View view, String username) {
          navigator.navigateToUserActivity(username);
        }

        @Override
        public void onEditReplyClick(android.view.View view, TopicReply reply) {
          if (PrefUtil.getToken(view.getContext()).getAccessToken() != null) {
            navigator.navigateToCreateReplyEdit(reply.getId(), title);
          } else {
            ToastUtils.showShort("登录后回复");
          }
        }

        @Override
        public void onLikeReplyClick(android.view.View view, TopicReply reply) {
          ToastUtils.showShort("此功能暂未实现");
        }

        @Override
        public void onReplyClick(android.view.View view, TopicReply reply, int floor) {
          if (PrefUtil.getToken(view.getContext()).getAccessToken() != null) {
            navigator
                .navigateToCreateReplyActivity(reply.getId(), title, reply.getUser().getLogin(),
                    floor);
          } else {
            ToastUtils.showShort("登录后回复");
          }
        }
      }, new Callback() {
        @Override
        public void clickUrl(String url) {
          if (url.contains("http")) {
            if (url.startsWith("https://www.diycode.cc/topics/")) {
              navigator.navigateToTopicActivity(Integer.valueOf(url.substring(30)));
              return;
            }
            navigator.navigateToWebActivity(url);
          } else if (url.startsWith("/")) {
            navigator.navigateToUserActivity(url.substring(1));
          }
        }

        @Override
        public void clickImage(String source) {
          navigator.navigateToImageActivity(source);
        }
      });
      view.setAdapter(adapter);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onReplyRefresh(ReplyEvent event) {
    view.onReplyRefresh();
  }

  public void getTopicReplies(int id, final boolean isRefresh) {
    if (isRefresh) {
      offset = 0;
    }
    boolean isEvictCache = isRefresh;
    if (isRefresh && isFirst) {
      isFirst = false;
      isEvictCache = false;
    }
    compositeSubscription.add(
        model.getTopicReplies(id, offset, isEvictCache)
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
            .subscribe(new ErrorHandleSubscriber<List<TopicReply>>(mErrorHandler) {
              @Override
              public void onNext(List<TopicReply> data) {
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
                view.setEmpty(mList.isEmpty());
              }

              @Override
              public void onError(Throwable e) {
                super.onError(e);
                view.onLoadMoreError();
              }
            })
    );
  }

  @Override
  public void onDestroy() {
    compositeSubscription.clear();
    adapter = null;
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
