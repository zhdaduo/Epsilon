package com.example.bill.epsilon.ui.topic.TopicList;


import static com.example.bill.epsilon.util.Constant.USERActivity_Create;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.event.CreateTopicEvent;
import com.example.bill.epsilon.bean.event.LogoutEvent;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.topic.TopicList.TopicListMVP.Model;
import com.example.bill.epsilon.ui.topic.TopicList.TopicListMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.util.RxUtil;
import com.example.bill.epsilon.view.adapter.TopicListAdapter;
import com.example.bill.epsilon.view.adapter.UserTopicAdapter;
import com.example.bill.epsilon.view.listener.ITopicListListener;
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
public class TopicListPresenter implements TopicListMVP.Presenter {

  private List<Topic> mList = new ArrayList<>();
  private int offset = 0;
  private boolean isFirst = true;
  private TopicListMVP.Model model;
  private TopicListMVP.View view;
  private RxErrorHandler mErrorHandler;
  private UserTopicAdapter adapter;
  private Navigator navigator;
  private List<Topic> topList = new ArrayList<>();

  @Inject
  public TopicListPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onCreteTopicRefresh(CreateTopicEvent event) {
    view.onCreteTopicRefresh();
  }

  public void initAdapter() {
    if (adapter == null) {
      adapter = new UserTopicAdapter(mList, new ITopicListListener() {
        @Override
        public void onUserItemClick(String username) {
          navigator.navigateToUserActivity(username);
        }

        @Override
        public void onItemClick(android.view.View view, int id) {
          navigator.navigateToTopicActivity(id);
        }

        @Override
        public void onItemLongClick(final android.view.View view, final Topic topic) {
          new MaterialDialog.Builder(view.getContext()).items("收藏")
              .itemsCallback(new ListCallback() {
                @Override
                public void onSelection(MaterialDialog dialog, android.view.View itemView,
                    int position, CharSequence text) {
                  if (!TextUtils.isEmpty(PrefUtil.getToken(view.getContext()).getAccessToken())) {
                    favoriteTopic(topic.getId());
                  } else {
                    Toast.makeText(view.getContext(), R.string.please_login, Toast.LENGTH_SHORT).show();
                  }
                }
              }).show();
        }
      });
      view.setAdapter(adapter);
    }
  }

  public void getTopics(final boolean isRefresh) {
    if (isRefresh) {
      offset = 0;
    }
    boolean isEvictCache = true;
        if (isRefresh && isFirst) {
            isFirst = false;
            isEvictCache = false;
        }
    model.getTopics(offset, isEvictCache)
        .compose(RxUtil.<List<Topic>>booleanSchedulers(view, isRefresh))
        .compose(RxUtil.<List<Topic>>bindToLifecycle(view))
        .subscribe(new ErrorHandleSubscriber<List<Topic>>(mErrorHandler) {
          @Override
          public void onNext(List<Topic> topics) {
            view.onLoadMoreComplete();
            if (offset == 0) {
              mList.clear();
              int size = topList.size();
              for (int i = 0; i < size; i++) {
                mList.add(i, topList.get(i));
                adapter.notifyDataSetChanged();
              }
            }
            mList.addAll(topics);
            adapter.notifyDataSetChanged();
            offset = adapter.getItemCount();
            if (topics.size() < Constant.PAGE_SIZE) {
              view.onLoadMoreEnd();
            }
          }

          @Override
          public void onError(Throwable e) {
            super.onError(e);
            view.onLoadMoreError();
          }
        });
  }

  public void getTopTopics() {
        model.getTopTopics()
        .compose(RxUtil.<List<Topic>>shortSchedulers())
        .compose(RxUtil.<List<Topic>>bindToLifecycle(view))
        .subscribe(new ErrorHandleSubscriber<List<Topic>>(mErrorHandler) {
          @Override
          public void onError(Throwable e) {
            super.onError(e);
          }

          @Override
          public void onNext(List<Topic> topics) {
            topList.addAll(topics);
          }
        });
  }

  public void favoriteTopic(int id) {
    model.favoriteTopic(id)
        .compose(RxUtil.<Ok>shortSchedulers())
        .compose(RxUtil.<Ok>bindToLifecycle(view))
        .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort(R.string.favorite_topic_error);
          }

          @Override
          public void onNext(@NonNull Ok ok) {
            view.onFavoriteSuccess();
          }
        });
  }

  @Override
  public void onDestroy() {
    adapter = null;
    view = null;
    mList = null;
    topList = null;
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

