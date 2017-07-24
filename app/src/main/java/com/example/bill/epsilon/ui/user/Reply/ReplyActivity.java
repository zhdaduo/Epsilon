package com.example.bill.epsilon.ui.user.Reply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.example.bill.epsilon.view.adapter.ReplyAdapter;
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

public class ReplyActivity extends BaseActivity implements ReplyMVP.View,
    SwipeRefreshLayout.OnRefreshListener {

  public static final String LOGIN_NAME = "username";
  private CompositeSubscription compositeSubscription;
  private Unbinder mUnbinder;
  private String mUsername;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.srl)
  SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.list)
  LoadMoreRecyclerView mRecyclerView;
  @BindView(R.id.tv_no_data)
  TextView mTvNoData;

  @Inject
  ReplyPresenter presenter;
  @Inject
  Navigator navigator;

  public static Intent getCallingIntent(Context context, String username) {
    Intent callingIntent = new Intent(context, ReplyActivity.class);
    callingIntent.putExtra(LOGIN_NAME, username);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_refresh);
    mUnbinder = ButterKnife.bind(this);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new ReplyModule(this))
        .inject(this);

    compositeSubscription = new CompositeSubscription();

    mToolbar.setTitle(R.string.my_reply);
    mToolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(mToolbar);

    mUsername = getIntent().getStringExtra(LOGIN_NAME);
    presenter.initAdapter();
    presenter.getUserReplies(mUsername, true);
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
  public void onRefresh() {
    presenter.getUserReplies(mUsername, true);
    mRecyclerView.setCanloadMore(true);
  }

  @Override
  public void setAdapter(ReplyAdapter adapter) {
    mRecyclerView.setAdapter(adapter);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    mSwipeRefreshLayout.setOnRefreshListener(this);
    LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
      @Override
      public void onLoadMore() {
        presenter.getUserReplies(mUsername, false);
      }
    });
    mRecyclerView.setOnClickReloadListener(new OnClickReloadListener() {
      @Override
      public void onClickReload() {
        mRecyclerView.setCanloadMore(true);
        mRecyclerView.showLoadMore();
        presenter.getUserReplies(mUsername, false);
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
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
