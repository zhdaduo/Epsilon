package com.example.bill.epsilon.ui.topic.Topic;

import static com.example.bill.epsilon.util.Constant.USERActivity_Create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.topic.Like;
import com.example.bill.epsilon.bean.event.LoadTopicDetailFinishEvent;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.util.TimeUtil;
import com.example.bill.epsilon.view.widget.webview.DWebView;
import java.text.MessageFormat;
import javax.inject.Inject;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Bill on 2017/7/19.
 */

public class TopicActivity extends BaseActivity implements TopicMVP.View {

  public static final String ID = "topicId";
  private int id;
  private Unbinder mUnbinder;
  private TopicDetail mTopic;

  @BindView(R.id.avatar)
  ImageView img_avatar;
  @BindView(R.id.name)
  TextView tv_name;
  @BindView(R.id.topic)
  TextView tv_topic;
  @BindView(R.id.tv_hit)
  TextView tv_hit;
  @BindView(R.id.time)
  TextView tv_time;
  @BindView(R.id.title)
  TextView tv_title;
  @BindView(R.id.toolbar)
  Toolbar mToolbar;
  @BindView(R.id.coordinator_layout)
  CoordinatorLayout mCoordinatorLayout;
  @BindView(R.id.progress_bar)
  ProgressBar mProgressBar;
  @BindView(R.id.normal_layout)
  LinearLayout mNormalLayout;
  @BindView(R.id.scroll_view)
  NestedScrollView mScrollView;
  @BindView(R.id.error_layout)
  LinearLayout mErrorLayout;
  @BindView(R.id.btn_like)
  ImageView mBtnLike;
  @BindView(R.id.tv_like_count)
  TextView mTvLikeCount;
  @BindView(R.id.btn_favorite)
  ImageView mBtnFavorite;
  @BindView(R.id.btn_reply)
  ImageView mBtnReply;
  @BindView(R.id.tv_reply_count)
  TextView mTvReplyCount;
  @BindView(R.id.content)
  DWebView content;
  @BindColor(R.color.colorAccent)
  int colorAccent;
  @BindColor(R.color.color_62646c)
  int color_62646c;

  @Inject
  TopicPresenter presenter;
  @Inject
  Navigator navigator;

  public static Intent getCallingIntent(Context context, int id) {
    Intent callingIntent = new Intent(context, TopicActivity.class);
    callingIntent.putExtra(ID, id);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_topic);
    mUnbinder = ButterKnife.bind(this);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new TopicModule(this))
        .inject(this);

    mToolbar.setTitle(R.string.topic);
    mToolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(mToolbar);

    mToolbar.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mScrollView.scrollTo(0, 0);
      }
    });

    if (getIntent() != null) {
      id = getIntent().getIntExtra(ID, 0);
    }

    presenter.getTopicDetail(id);
  }

  @OnClick({R.id.avatar, R.id.name})
  void onUserClick() {
    navigator.navigateToUserActivity(mTopic.getUser().getLogin());
  }

  @OnClick(R.id.btn_like)
  void onLikeClick() {
    if (!hasSignedIn()) {
      return;
    }
    if (mTopic.isLiked()) {
      presenter.unlikeTopic(mTopic.getId());
    } else {
      presenter.likeTopic(mTopic.getId());
    }
  }

  @OnClick(R.id.btn_favorite)
  void onFavoriteClick() {
    if (!hasSignedIn()) {
      return;
    }
    if (mTopic.isFavorited()) {
      presenter.unfavoriteTopic(mTopic.getId());
    } else {
      presenter.favoriteTopic(mTopic.getId());
    }
  }

  @OnClick(R.id.btn_reply)
  void onReplyClick() {
    navigator.navigateToTopicReplyActivity(mTopic.getId(), mTopic.getTitle());
  }

  @OnClick(R.id.reloading)
  void onReloading() {
    presenter.getTopicDetail(id);
  }

  @Override
  public void showLoading() {
    mProgressBar.setVisibility(View.VISIBLE);
    mNormalLayout.setVisibility(View.GONE);
    mErrorLayout.setVisibility(View.GONE);
  }

  @Override
  public void hideLoading() {
    mProgressBar.setVisibility(View.GONE);
    mNormalLayout.setVisibility(View.VISIBLE);
  }

  @Override
  public void showMessage(String message) {
    ToastUtils.showShort(message);
  }

  @Override
  public void onGetTopicDetail(TopicDetail topic) {
    Context context = getApplicationContext();
    if (topic == null) {
      return;
    }
    this.mTopic = topic;
    tv_name.setText(mTopic.getUser().getLogin());
    tv_time.setText(MessageFormat
        .format(getString(R.string.publish_time),
            TimeUtil.computePastTime(mTopic.getUpdatedAt())));
    tv_topic.setText(mTopic.getNodeName());
    tv_title.setText(mTopic.getTitle());
    tv_hit.setText(
        MessageFormat.format(getResources().getString(R.string.read_count), mTopic.getHits()));
    Glide.with(context)
        .load(mTopic.getUser().getAvatarUrl())
        .bitmapTransform(new CropCircleTransformation(context))
        .placeholder(R.drawable.shape_glide_img_error)
        .error(R.drawable.shape_glide_img_error)
        .crossFade()
        .into(img_avatar);
    content.loadDetailDataAsync(topic.getBodyHtml(), new Runnable() {
      @Override
      public void run() {
        EventBus.getDefault().post(new LoadTopicDetailFinishEvent());
      }
    });
    if (mTopic.getRepliesCount() == 0) {
      mTvReplyCount.setText("");
    } else {
      mTvReplyCount.setText(String.valueOf(mTopic.getRepliesCount()));
    }
    mBtnReply.setImageResource(R.drawable.ic_fab_reply);
    mBtnLike.setImageResource(mTopic.isLiked() ? R.drawable.ic_like_yes : R.drawable.ic_like);
    mTvLikeCount.setTextColor(mTopic.isLiked() ? colorAccent : color_62646c);
    mTvLikeCount.setText(mTopic.getLikesCount() > 0 ? String.valueOf(mTopic.getLikesCount()) : "");
    mBtnFavorite.setImageResource(
        mTopic.isFavorited() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite);
  }

  @Override
  public void onFavoriteTopic() {
    mTopic.setFavorited(!mTopic.isFavorited());
    mBtnFavorite.setImageResource(
        mTopic.isFavorited() ? R.drawable.ic_favorite_yes : R.drawable.ic_favorite);
    if (mTopic.isFavorited()) {
      Snackbar.make(mCoordinatorLayout, "已收藏", Snackbar.LENGTH_SHORT)
          .setAction("查看我的收藏", new OnClickListener() {
            @Override
            public void onClick(View v) {
              if (!TextUtils.isEmpty(PrefUtil.getToken(getApplicationContext()).getAccessToken())) {
                navigator.navigateToUserFavoriteTopic(PrefUtil.getMe(getApplicationContext()).getLogin());
              }
            }
          }).show();
    }
  }

  @Override
  public void onLikeTopic(Like like) {
    mTopic.setLiked(!mTopic.isLiked());
    mBtnLike.setImageResource(mTopic.isLiked() ? R.drawable.ic_like_yes : R.drawable.ic_like);
    mTvLikeCount.setTextColor(mTopic.isLiked() ? colorAccent : color_62646c);
    mTvLikeCount.setText(like.getCount() > 0 ? String.valueOf(like.getCount()) : "");
  }

  @Override
  public void setLayout(boolean isNormal) {
    mNormalLayout.setVisibility(isNormal ? View.VISIBLE : View.GONE);
    mErrorLayout.setVisibility(isNormal ? View.GONE : View.VISIBLE);
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
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_toolbar_detail_activity, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finish();
    } else if (id == R.id.action_share) {
      navigator.shareText(TopicActivity.this, mTopic.getTitle(), "https://www.diycode.cc/topics/" + mTopic.getId());
    } else if (id == R.id.action_open_web) {
      navigator.navigateToBrower("https://www.diycode.cc/topics/" + mTopic.getId());
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (presenter != null) {
      presenter.onStart();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (presenter != null) {
      presenter.onStop();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    presenter.onDestroy();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
      mUnbinder.unbind();
    }
    this.mUnbinder = null;
  }
}
