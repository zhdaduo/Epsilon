package com.example.bill.epsilon.ui.topic.UserTopic;

import static com.example.bill.epsilon.util.Constant.TYPE_CREATE;
import static com.example.bill.epsilon.util.Constant.TYPE_FAVORITE;
import static com.example.bill.epsilon.util.Constant.USERActivity_Create;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.event.FavoriteTopicEvent;
import com.example.bill.epsilon.bean.event.UnFavoriteTopicEvent;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.topic.UserTopic.UserTopicMVP.Model;
import com.example.bill.epsilon.ui.topic.UserTopic.UserTopicMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.util.RxUtil;
import com.example.bill.epsilon.view.adapter.TopicListAdapter;
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
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/18.
 */
@PerFragment
public class UserTopicPresenter implements UserTopicMVP.Presenter {

  private List<Topic> mList = new ArrayList<>();
  private int offset = 0;
  private UserTopicMVP.Model model;
  private UserTopicMVP.View view;
  private RxErrorHandler mErrorHandler;
  private TopicListAdapter adapter;
  private Navigator navigator;

  @Inject
  public UserTopicPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
  }

  public void initAdapter(final int topicType, final String username) {
    if (adapter == null) {
      adapter = new TopicListAdapter(mList, new ITopicListListener() {
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
          switch (topicType) {
            case TYPE_CREATE:
              new MaterialDialog.Builder(view.getContext()).items("收藏")
                  .itemsCallback(new ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, android.view.View itemView,
                        int position, CharSequence text) {
                      if (!TextUtils.isEmpty(PrefUtil.getToken(view.getContext()).getAccessToken())) {
                        favoriteTopic(topic);
                      } else {
                        ToastUtils.showShort(R.string.please_login);
                      }
                    }
                  }).show();
              break;
            case TYPE_FAVORITE:
              new MaterialDialog.Builder(view.getContext()).items("取消收藏")
                  .itemsCallback(new ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, android.view.View itemView,
                        int position, CharSequence text) {
                      if (!TextUtils.isEmpty(PrefUtil.getToken(view.getContext()).getAccessToken())
                      && username.equals(PrefUtil.getMe(view.getContext()).getLogin())) {
                        unfavoriteTopic(topic);
                      } else {
                        ToastUtils.showShort(R.string.please_login);
                      }
                    }
                  }).show();
              break;
            default:
              break;
          }
        }

      });
      view.setAdapter(adapter);
    }
  }

  @Subscribe(threadMode = ThreadMode.MAIN) public void onFollowRefresh(FavoriteTopicEvent event) {
    view.onFavoriteRefresh();
  }

  public void getTopics(int topicType, String username, final boolean isRefresh) {
    if (isRefresh) {
      offset = 0;
    }
    Observable<List<Topic>> observable;
    switch (topicType) {
      case TYPE_CREATE:
        observable = model.getUserTopics(username, offset);
        break;
      case TYPE_FAVORITE:
        observable = model.getUserFavorites(username, offset);
        break;
      default:
        observable = model.getUserTopics(username, offset);
        break;
    }
        observable.subscribeOn(Schedulers.io())
            .compose(RxUtil.<List<Topic>>booleanSchedulers(view, isRefresh))
            .compose(RxUtil.<List<Topic>>bindToLifecycle(view))
            .subscribe(new ErrorHandleSubscriber<List<Topic>>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.onLoadMoreError();
              }

              @Override
              public void onNext(@NonNull List<Topic> data) {
                view.onLoadMoreComplete();
                if (offset == 0) mList.clear();
                mList.addAll(data);
                adapter.notifyDataSetChanged();
                offset = adapter.getItemCount();
                if (data.size() < Constant.PAGE_SIZE) view.onLoadMoreEnd();
                view.setEmpty(mList.isEmpty());
              }
            });
  }

  public void favoriteTopic(final Topic topic) {
    model.favoriteTopic(topic.getId())
        .compose(RxUtil.<Ok>shortSchedulers())
        .compose(RxUtil.<Ok>bindToLifecycle(view))
        .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("收藏失败");
          }

          @Override
          public void onNext(@NonNull Ok ok) {
            EventBus.getDefault().post(new FavoriteTopicEvent());
            //refresh userActivity
            EventBus.getDefault().post(new UnFavoriteTopicEvent());
            view.onFavoriteSuccess();
          }
        });
  }

  private void unfavoriteTopic(final Topic topic) {
    model.unfavoriteTopic(topic.getId())
        .compose(RxUtil.<Ok>shortSchedulers())
        .compose(RxUtil.<Ok>bindToLifecycle(view))
        .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
          @Override
          public void onError(@NonNull Throwable e) {
            super.onError(e);
            ToastUtils.showShort("取消收藏失败");
          }

          @Override
          public void onNext(@NonNull Ok ok) {
            view.showMessage("已取消收藏");
            EventBus.getDefault().post(new UnFavoriteTopicEvent());
            mList.remove(topic);
            adapter.notifyDataSetChanged();
            view.setEmpty(mList.isEmpty());
          }
        });
  }

  @Override
  public void onDestroy() {
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
