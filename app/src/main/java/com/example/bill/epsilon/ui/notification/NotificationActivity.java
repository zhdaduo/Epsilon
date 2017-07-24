package com.example.bill.epsilon.ui.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.view.adapter.NotificationAdapter;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreFooter.OnClickReloadListener;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreListener;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreRecyclerView;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/20.
 */

public class NotificationActivity extends BaseActivity implements NotificationMVP.View
    , SwipeRefreshLayout.OnRefreshListener {

  private Unbinder mUnbinder;
  private CompositeSubscription compositeSubscription;


  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.srl)
  SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.list)
  LoadMoreRecyclerView mRecyclerView;
  @BindView(R.id.tv_no_data)
  TextView mTvNoData;

  @BindColor(R.color.color_4d4d4d)
  int color_4d4d4d;
  @BindColor(R.color.color_999999)
  int color_999999;

  @Inject
  NotificationPresenter presenter;

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, NotificationActivity.class);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);
    mUnbinder = ButterKnife.bind(this);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new NotificationModule(this))
        .inject(this);

    compositeSubscription = new CompositeSubscription();

    mToolbar.setTitle(R.string.notification);
    mToolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(mToolbar);

    presenter.initAdapter();
    presenter.getNotifications(true);
  }

  @Override
  public void onRefresh() {
    presenter.getNotifications(true);
    mRecyclerView.setCanloadMore(true);
  }

  @Override
  public void showLoading() {
    compositeSubscription.add(
    Observable.just(1)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Integer>() {
          @Override
          public void call(Integer integer) {
            mSwipeRefreshLayout.setRefreshing(true);
          }
        })
    );
  }

  @Override
  public void hideLoading() {
    mSwipeRefreshLayout.setRefreshing(false);
  }

  @Override
  public void showMessage(String message) {
    ToastUtils.showShort(message);
  }

  @Override
  public void setAdapter(NotificationAdapter adapter) {
    mRecyclerView.setAdapter(adapter);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    mSwipeRefreshLayout.setOnRefreshListener(this);
    LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
      @Override
      public void onLoadMore() {
        presenter.getNotifications(false);
      }
    });
    mRecyclerView.setOnClickReloadListener(new OnClickReloadListener() {
      @Override
      public void onClickReload() {
        mRecyclerView.setCanloadMore(true);
        mRecyclerView.showLoadMore();
        presenter.getNotifications(false);
      }
    });
  }

  @Override
  public void setEmpty(boolean isEmpty) {
    mRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    mTvNoData.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
  }

  @Override
  public void onLoadMoreComplete() {
    mRecyclerView.loadMoreComplete();
  }

  @Override
  public void onLoadMoreError() {
    mRecyclerView.loadMoreError();
    mRecyclerView.setCanloadMore(false);
  }

  @Override
  public void onLoadMoreEnd() {
    mRecyclerView.loadMoreEnd();
    mRecyclerView.setCanloadMore(false);
  }

  @Override
  public void setSubtitle(String subtitle) {
    mToolbar.setSubtitle(subtitle);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_notification, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finish();
    } else if (id == R.id.action_clear_all) {
      new MaterialDialog.Builder(this)
          .content("确定要清空全部通知吗？")
          .contentColor(color_4d4d4d)
          .positiveText(R.string.confirm)
          .onPositive(new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
              presenter.deleteAllNotifications();
            }
          })
          .negativeText(R.string.cancel)
          .negativeColor(color_999999)
          .show();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    compositeSubscription.clear();
    presenter.onDestroy();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
      mUnbinder.unbind();
    }
    this.mUnbinder = null;
  }
}
