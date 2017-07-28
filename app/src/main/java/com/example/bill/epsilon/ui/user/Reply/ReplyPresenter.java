package com.example.bill.epsilon.ui.user.Reply;

import com.example.bill.epsilon.bean.topic.Reply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.user.Reply.ReplyMVP.Model;
import com.example.bill.epsilon.ui.user.Reply.ReplyMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.RxUtil;
import com.example.bill.epsilon.util.html.HtmlUtils.Callback;
import com.example.bill.epsilon.view.adapter.ReplyAdapter;
import com.example.bill.epsilon.view.adapter.ReplyAdapter.OnItemClickListener;
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
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class ReplyPresenter implements ReplyMVP.Presenter {

  private List<Reply> mList = new ArrayList<>();
  private ReplyAdapter adapter;
  private ReplyMVP.Model model;
  private ReplyMVP.View view;
  private RxErrorHandler mErrorHandler;
  private Navigator navigator;
  private int offset = 0;
  private boolean isFirst = true;

  @Inject
  public ReplyPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
  }

  public void initAdapter() {
    if (adapter == null) {
      adapter = new ReplyAdapter(mList, new OnItemClickListener() {
        @Override
        public void onItemClick(android.view.View view, Reply reply) {
          navigator.navigateToTopicActivity(reply.getTopicId());
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

  public void getUserReplies(String username, final boolean isRefresh) {
    if (isRefresh) {
      offset = 0;
    }
    boolean isEvictCache = isRefresh;
        if (isRefresh && isFirst) {
            isFirst = false;
            isEvictCache = false;
        }
        model.getUserReplies(username, offset, isEvictCache)
            .compose(RxUtil.<List<Reply>>booleanSchedulers(view, isRefresh))
            .compose(RxUtil.<List<Reply>>bindToLifecycle(view))
            .subscribe(new ErrorHandleSubscriber<List<Reply>>(mErrorHandler) {
              @Override
              public void onNext(List<Reply> data) {
                view.onLoadMoreComplete();
                if (offset == 0) mList.clear();
                mList.addAll(data);
                adapter.notifyDataSetChanged();
                offset = adapter.getItemCount();
                if (data.size() < Constant.PAGE_SIZE) view.onLoadMoreEnd();
                view.setEmpty(mList.isEmpty());
              }

              @Override
              public void onError(Throwable e) {
                super.onError(e);
                view.onLoadMoreError();
              }
            });
  }

  @Override
  public void onDestroy() {
    adapter = null;
    mList = null;
    mErrorHandler = null;
  }
}
