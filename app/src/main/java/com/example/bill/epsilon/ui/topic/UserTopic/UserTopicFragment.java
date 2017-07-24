package com.example.bill.epsilon.ui.topic.UserTopic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.base.BaseFragment;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.view.adapter.TopicListAdapter;
import com.example.bill.epsilon.view.widget.LinearDecoration;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreFooter.OnClickReloadListener;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreListener;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreRecyclerView;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/18.
 */

public class UserTopicFragment extends BaseFragment implements UserTopicMVP.View,
    SwipeRefreshLayout.OnRefreshListener {

  public static final String TYPE = "type";
  public static final String User = "user";
  private Unbinder mUnbinder;
  private CompositeSubscription compositeSubscription;
  private int type;
  private String username;

  @BindView(R.id.srl) SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.list) LoadMoreRecyclerView mRecyclerView;
  @BindView(R.id.tv_no_data) TextView mTvNoData;

  @Inject UserTopicPresenter presenter;
  @Inject Navigator navigator;

  public static UserTopicFragment newInstance(String username, int type) {
    UserTopicFragment fragment = new UserTopicFragment();
    Bundle bundle = new Bundle();
    bundle.putString(User, username);
    bundle.putInt(TYPE, type);
    fragment.setArguments(bundle);
    return fragment;
  }

  public UserTopicFragment() {
    setRetainInstance(true);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((AndroidApplication) getActivity().getApplication()).getApplicationComponent()
        .plus(new UserTopicModule(this)).inject(this);

    compositeSubscription = new CompositeSubscription();

    Bundle bundle = getArguments();
    if (bundle != null) {
      username = bundle.getString(User);
      type = bundle.getInt(TYPE);
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_usertopic, container, false);
    mUnbinder = ButterKnife.bind(this, rootView);

    return rootView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    presenter.initAdapter(type, username);
    presenter.getTopics(type, username,true);
  }

  @Override
  public void fetchData() {
    //presenter.getTopics(type, username,true);
  }

  @Override
  public void onRefresh() {
    presenter.getTopics(type, username,true);
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
  public void setAdapter(TopicListAdapter adapter) {
    mRecyclerView.setAdapter(adapter);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    mSwipeRefreshLayout.setOnRefreshListener(this);
    mRecyclerView.addItemDecoration(new LinearDecoration(getContext(), RecyclerView.VERTICAL, 1));
    LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
      @Override
      public void onLoadMore() {
        presenter.getTopics(type, username,false);
      }
    });
    mRecyclerView.setOnClickReloadListener(new OnClickReloadListener() {
      @Override
      public void onClickReload() {
        mRecyclerView.setCanloadMore(true);
        mRecyclerView.showLoadMore();
        presenter.getTopics(type, username,false);
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
  public void onFavoriteSuccess() {
    ToastUtils.showShort("已收藏");
  }

  @Override
  public void onFavoriteRefresh() {
    if (PrefUtil.getMe(getActivity()).getLogin().equals(username))
    {
      presenter.getTopics(type, username,true);
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override
  public void onStop() {
    super.onStop();
    presenter.onStop();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
      mUnbinder.unbind();
    this.mUnbinder = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    presenter.onDestroy();
    compositeSubscription.clear();
  }
}
