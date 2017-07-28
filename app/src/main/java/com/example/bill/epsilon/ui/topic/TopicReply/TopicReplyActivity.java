package com.example.bill.epsilon.ui.topic.TopicReply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.bill.epsilon.view.adapter.TopicReplyAdapter;
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

public class TopicReplyActivity extends BaseActivity implements TopicReplyMVP.View
    , SwipeRefreshLayout.OnRefreshListener {

  public static final String TOPICID = "topicID";
  public static final String TOPICTITLE = "topicTITLE";
  private CompositeSubscription compositeSubscription;
  private Unbinder mUnbinder;
  private int id;
  private String title;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.srl) SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.list)
  LoadMoreRecyclerView mRecyclerView;
  @BindView(R.id.tv_no_data)
  TextView mTvNoData;
  @BindView(R.id.fab)
  FloatingActionButton mFab;

  @Inject TopicReplyPresenter presenter;
  @Inject
  Navigator navigator;

  public static Intent getCallingIntent(Context context, int id, String title) {
    Intent callingIntent = new Intent(context, TopicReplyActivity.class);
    callingIntent.putExtra(TOPICID, id);
    callingIntent.putExtra(TOPICTITLE, title);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list_refresh);
    mUnbinder = ButterKnife.bind(this);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new TopicReplyModule(this))
        .inject(this);

    compositeSubscription = new CompositeSubscription();

    mToolbar.setTitle(R.string.reply);
    mToolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(mToolbar);
    mFab.setVisibility(View.VISIBLE);

    id = getIntent().getIntExtra(TOPICID, 0);
    title = getIntent().getStringExtra(TOPICTITLE);
    presenter.initAdapter(title);
    presenter.getTopicReplies(id, true);
  }

  @Override
  public void onRefresh() {
    presenter.getTopicReplies(id, true);
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

  @OnClick(R.id.fab)
  void OnCreateReply() {
    navigator.navigateToCreateReply(id, title);
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
  public void setAdapter(TopicReplyAdapter adapter) {
    mRecyclerView.setAdapter(adapter);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    mSwipeRefreshLayout.setOnRefreshListener(this);
    LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
      @Override
      public void onLoadMore() {
        presenter.getTopicReplies(id, false);
      }
    });
    mRecyclerView.setOnClickReloadListener(new OnClickReloadListener() {
      @Override
      public void onClickReload() {
        mRecyclerView.setCanloadMore(true);
        mRecyclerView.showLoadMore();
        presenter.getTopicReplies(id, false);
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
  public void onReplyRefresh() {
    presenter.getTopicReplies(id, true);
    mRecyclerView.setCanloadMore(true);
  }

  @Override
  public boolean isSupportSwipeBack() {
    return false;
  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.onStop();
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
