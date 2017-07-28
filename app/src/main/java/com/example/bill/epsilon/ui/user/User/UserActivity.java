package com.example.bill.epsilon.ui.user.User;

import static com.example.bill.epsilon.util.Constant.TYPE_CREATE;
import static com.example.bill.epsilon.util.Constant.TYPE_FAVORITE;
import static com.example.bill.epsilon.util.Constant.USERActivity_Create;
import static com.example.bill.epsilon.util.Constant.USERActivity_Favorite;
import static com.example.bill.epsilon.util.Constant.USERActivity_Follow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.example.bill.epsilon.ui.topic.UserTopic.UserTopicFragment;
import com.example.bill.epsilon.ui.user.UserFollow.UserFollowFragment;
import com.example.bill.epsilon.util.PrefUtil;
import com.jaeger.library.StatusBarUtil;
import javax.inject.Inject;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Bill on 2017/7/18.
 */

public class UserActivity extends BaseActivity implements UserMVP.View {

  public static final String LOGIN_NAME = "loginName";
  private Unbinder mUnbinder;
  private String username;
  private FragmentManager fragmentManager;
  private UserTopicFragment UserCreateTopic;
  private UserTopicFragment UserFavoriteTopic;
  private UserFollowFragment UserFollow;
  private String[] fragNames;

  @BindView(R.id.avatar) ImageView avatar;
  @BindView(R.id.name) TextView name;
  @BindView(R.id.topic_num) TextView topic;
  @BindView(R.id.favorite_num) TextView favorite;
  @BindView(R.id.followed_num) TextView followed;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.follow) Button follow;
  @BindView(R.id.coordinator)
  CoordinatorLayout mCoordinatorLayout;

  @Inject UserPresenter presenter;
  @Inject
  Navigator navigator;

  public static Intent getCallingIntent(Context context, String loginName) {
    Intent callingIntent = new Intent(context, UserActivity.class);
    callingIntent.putExtra(LOGIN_NAME, loginName);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    setContentView(R.layout.activity_user);
    StatusBarUtil.setColorForSwipeBack(this, 0x515a74);
    mUnbinder = ButterKnife.bind(this);
    super.onCreate(savedInstanceState);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new UserModule(this))
        .inject(this);

    setSupportActionBar(toolbar);

    if (getIntent() != null) {
      username = getIntent().getStringExtra(LOGIN_NAME);
    }
    presenter.getUser(username);

    follow.setBackground(getResources().getDrawable(R.drawable.shape_user_follow_bg));
    follow.setTextColor(getResources().getColor(R.color.white));
    follow.setText("+关注");

    fragmentManager = getSupportFragmentManager();
    fragNames = new String[]{ "TYPE_CREATE", "TYPE_FAVORITE", "TYPE_FOLLOW"};

    onCreateTopic();
  }

  @OnClick(R.id.back) void OnBack() {
    finish();
  }

  @OnClick(R.id.topic) void showCreateTopic() {
    onCreateTopic();
  }

  public void onCreateTopic() {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    hideAllFrags(fragmentTransaction);
    if (UserCreateTopic == null) {
      UserCreateTopic =   UserTopicFragment.newInstance(username, TYPE_CREATE);
      fragmentTransaction.add(R.id.container, UserCreateTopic, fragNames[0]);
    } else {
      fragmentTransaction.show(UserCreateTopic);
    }
    fragmentTransaction.commit();
  }

  @OnClick(R.id.favorite) void showFavoriteTopic() {
    onFavoriteTopic();
  }

  public void onFavoriteTopic() {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    hideAllFrags(fragmentTransaction);
    if (UserFavoriteTopic == null) {
      UserFavoriteTopic = UserTopicFragment.newInstance(username, TYPE_FAVORITE);
      fragmentTransaction.add(R.id.container, UserFavoriteTopic, fragNames[1]);
    } else {
      fragmentTransaction.show(UserFavoriteTopic);
    }
    fragmentTransaction.commit();
  }

  @OnClick(R.id.followed) void showFollowedUser() {
    onFollowedUser();
  }

  public void onFollowedUser() {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    hideAllFrags(fragmentTransaction);
    if (UserFollow == null) {
      UserFollow = UserFollowFragment.newInstance(username);
      fragmentTransaction.add(R.id.container, UserFollow, fragNames[2]);
    } else {
      fragmentTransaction.show(UserFollow);
    }
    fragmentTransaction.commit();
  }

  private void hideAllFrags(FragmentTransaction fragmentTransaction) {
    for (String name : fragNames) {
      Fragment fragment = fragmentManager.findFragmentByTag(name);
      if (fragment != null && !fragment.isHidden()) {
        fragmentTransaction.hide(fragment);
      }
    }
  }

  @OnClick(R.id.follow) void updateFollow() {
    if(!hasSignedIn()) return;
    if (follow.getText().equals("已关注")) {
      presenter.unfollowUser(username);
    } else {
      presenter.followUser(username);
    }
  }

  @Override
  public void onGetUserInfo(UserDetailInfo user) {
    if (user != null && user.getLogin().equals(username)) {
      name.setText(user.getLogin());
      topic.setText(String.valueOf(user.getTopicsCount()));
      followed.setText(String.valueOf(user.getFollowingCount()));
      favorite.setText(String.valueOf(user.getFavoritesCount()));
      Glide.with(this)
          .load(user.getAvatarUrl())
          .bitmapTransform(new CropCircleTransformation(this))
          .crossFade()
          .placeholder(R.drawable.shape_glide_img_error)
          .error(R.drawable.shape_glide_img_error)
          .into(avatar);
    }
  }

  @Override
  public void onFollowUser() {
    follow.setBackground(getResources().getDrawable(R.drawable.shape_user_followed_bg));
    follow.setTextColor(getResources().getColor(R.color.color_secondary_999999));
    follow.setText("已关注");
    ToastUtils.showShort("已关注");
  }

  @Override
  public void onUnFollowUser() {
    follow.setBackground(getResources().getDrawable(R.drawable.shape_user_follow_bg));
    follow.setTextColor(getResources().getColor(R.color.white));
    follow.setText("+关注");
    ToastUtils.showShort("已取消关注");
  }

  @Override
  public void onFreshUserTopic() {
    presenter.getUser(username);
  }

  private boolean hasSignedIn() {
    if (TextUtils.isEmpty(PrefUtil.getToken(this).getAccessToken())) {
      Snackbar.make(mCoordinatorLayout, "请先登录", Snackbar.LENGTH_LONG)
          .setAction("登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              navigator.navigateToSignActivity();
            }
          })
          .show();
      return false;
    } else {
      return true;
    }
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
    presenter.onDestroy();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
      mUnbinder.unbind();
    this.mUnbinder = null;
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

  }

  @Override
  public void showMessage(String message) {

  }
}
