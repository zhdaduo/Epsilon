package com.example.bill.epsilon.ui.user.UserFollow;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.bill.epsilon.view.adapter.UserAdapter;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreFooter.OnClickReloadListener;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreListener;
import com.example.bill.epsilon.view.widget.loadmore.LoadMoreRecyclerView;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/19.
 */

public class UserFollowFragment extends BaseFragment implements UserFollowMVP.View,
    SwipeRefreshLayout.OnRefreshListener {

  public static final String User = "user";
  private Unbinder mUnbinder;
  private CompositeSubscription compositeSubscription;
  private String username;

  @BindView(R.id.srl) SwipeRefreshLayout mSwipeRefreshLayout;
  @BindView(R.id.list) LoadMoreRecyclerView mRecyclerView;
  @BindView(R.id.tv_no_data) TextView mTvNoData;

  @Inject UserFollowPresenter presenter;
  @Inject Navigator navigator;

  public static UserFollowFragment newInstance(String username) {
    UserFollowFragment fragment = new UserFollowFragment();
    Bundle bundle = new Bundle();
    bundle.putString(User, username);
    fragment.setArguments(bundle);
    return fragment;
  }

  public UserFollowFragment() {
    setRetainInstance(true);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ((AndroidApplication) getActivity().getApplication()).getApplicationComponent()
        .plus(new UserFollowModule(this)).inject(this);

    compositeSubscription = new CompositeSubscription();

    Bundle bundle = getArguments();
    if (bundle != null) {
      username = bundle.getString(User);
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
    presenter.initAdapter(username);
    presenter.getUsers(username, true);
  }

  @Override
  public void fetchData() {}

  @Override
  public void onRefresh() {
    presenter.getUsers(username, true);
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
  public void setAdapter(UserAdapter adapter) {
    mRecyclerView.setAdapter(adapter);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    mSwipeRefreshLayout.setOnRefreshListener(this);
    LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    mRecyclerView.setLayoutManager(manager);
    mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
      @Override
      public void onLoadMore() {
        presenter.getUsers(username,false);
      }
    });
    mRecyclerView.setOnClickReloadListener(new OnClickReloadListener() {
      @Override
      public void onClickReload() {
        mRecyclerView.setCanloadMore(true);
        mRecyclerView.showLoadMore();
        presenter.getUsers(username,false);
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
  public void onFollowRefresh() {
    if (PrefUtil.getMe(getActivity()).getLogin().equals(username))
    {
      presenter.getUsers(username, true);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
      mUnbinder.unbind();
    this.mUnbinder = null;
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
  public void onDestroy() {
    super.onDestroy();
    presenter.onDestroy();
    compositeSubscription.clear();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    setUserVisibleHint(true);
  }
}
