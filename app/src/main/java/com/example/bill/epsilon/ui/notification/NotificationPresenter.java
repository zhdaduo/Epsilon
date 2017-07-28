package com.example.bill.epsilon.ui.notification;

import static com.example.bill.epsilon.util.Constant.USERActivity_Create;

import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.event.GetUnreadCountEvent;
import com.example.bill.epsilon.bean.notification.Notification;
import com.example.bill.epsilon.bean.event.NotificationsUnreadCount;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.notification.NotificationMVP.Model;
import com.example.bill.epsilon.ui.notification.NotificationMVP.View;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.html.HtmlUtils.Callback;
import com.example.bill.epsilon.view.adapter.NotificationAdapter;
import com.example.bill.epsilon.view.adapter.NotificationAdapter.OnItemClickListener;
import com.example.bill.epsilon.view.adapter.NotificationAdapter.OnItemLongClickListener;
import java.util.ArrayList;
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
public class NotificationPresenter implements NotificationMVP.Presenter {

  private List<Notification> mList = new ArrayList<>();
  private int offset = 0;
  private boolean isFirst = true;
  private CompositeSubscription compositeSubscription;
  private NotificationMVP.Model model;
  private NotificationMVP.View view;
  private RxErrorHandler mErrorHandler;
  private NotificationAdapter adapter;
  private Navigator navigator;

  @Inject
  public NotificationPresenter(Model model,
      View view, RxErrorHandler handler, Navigator navigator) {
    this.model = model;
    this.view = view;
    this.mErrorHandler = handler;
    this.navigator = navigator;
    compositeSubscription = new CompositeSubscription();
  }

  public void initAdapter() {
    if (adapter == null) {
      adapter = new NotificationAdapter(mList, new OnItemLongClickListener() {
        @Override
        public void onItemLongClick(android.view.View view, final Notification notification) {
          new MaterialDialog.Builder(view.getContext()).items("删除")
              .itemsCallback(new ListCallback() {
                @Override
                public void onSelection(MaterialDialog dialog, android.view.View itemView,
                    int position, CharSequence text) {
                  deleteNotification(notification);
                }
              }).show();
        }
      },
          new OnItemClickListener() {
            @Override
            public void onItemClick(android.view.View view, Notification notification) {
              getUnreadCount();
              switch (notification.getType()) {
                case "TopicReply":
                  navigator.navigateToTopicActivity(notification.getReply().getTopicId());
                  break;
                case "Mention":
                  navigator.navigateToTopicActivity(notification.getMention().getTopicId());
                  break;
                case "Topic":
                case "NodeChanged":
                  navigator.navigateToTopicActivity(notification.getTopic().getId());
                  break;
                case "Hacknews":
                  break;
                default:
                  break;
              }
            }

            @Override
            public void onNameClick(android.view.View view, String username) {
              navigator.navigateToUserActivity(username);
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

  public void getNotifications(final boolean isRefresh) {

    if (isRefresh) {
      offset = 0;
    }
    boolean isEvictCache = isRefresh;
    if (isRefresh && isFirst) {
      isFirst = false;
      isEvictCache = false;
    }
    compositeSubscription.add(
        model.getNotifications(offset, isEvictCache)
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
            .subscribe(new ErrorHandleSubscriber<List<Notification>>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.onLoadMoreError();
              }

              @Override
              public void onNext(@NonNull List<Notification> data) {
                view.onLoadMoreComplete();
                if (offset == 0) {
                  mList.clear();
                  getUnreadCount();
                }
                mList.addAll(data);
                adapter.notifyDataSetChanged();
                offset = adapter.getItemCount();
                if (data.size() < Constant.PAGE_SIZE) {
                  view.onLoadMoreEnd();
                }
                view.setEmpty(mList.isEmpty());
              }
            })
    );
  }

  public void deleteNotification(final Notification notification) {
    compositeSubscription.add(
        model.deleteNotification(notification.getId())
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                ToastUtils.showShort("删除失败");
              }

              @Override
              public void onNext(@NonNull Ok ok) {
                mList.remove(notification);
                adapter.notifyDataSetChanged();
                getUnreadCount();
              }
            })
    );
  }

  public void deleteAllNotifications() {
    compositeSubscription.add(
        model.deleteAllNotifications()
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                ToastUtils.showShort("删除失败");
              }

              @Override
              public void onNext(@NonNull Ok ok) {
                mList.clear();
                adapter.notifyDataSetChanged();
                getUnreadCount();
              }
            })
    );
  }

  public void getUnreadCount() {
    compositeSubscription.add(
        model.getUnreadCount()
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ErrorHandleSubscriber<NotificationsUnreadCount>(mErrorHandler) {
              @Override
              public void onNext(@NonNull NotificationsUnreadCount data) {
                int count = data.getCount();
                if (count > 0) {
                  view.setSubtitle(data.getCount() + "条未读");
                } else {
                  view.setSubtitle("没有未读的通知");
                }
                EventBus.getDefault()
                    .post(new GetUnreadCountEvent(data.getCount() > 0 ? true : false));
              }
            })
    );
  }

  public void readNotification(int[] ids) {
    compositeSubscription.add(
        model.readNotification(ids)
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new ErrorHandleSubscriber<Ok>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                ToastUtils.showShort("设置已读失败");
              }

              @Override
              public void onNext(@NonNull Ok data) {
                ToastUtils.showShort("设置已读成功");
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
