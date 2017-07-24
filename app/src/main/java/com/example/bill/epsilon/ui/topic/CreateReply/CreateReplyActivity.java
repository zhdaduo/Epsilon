package com.example.bill.epsilon.ui.topic.CreateReply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.util.HtmlUtil;
import java.text.MessageFormat;
import javax.inject.Inject;

/**
 * Created by Bill on 2017/7/20.
 */

public class CreateReplyActivity extends AppCompatActivity implements CreateReplyMVP.View {

  public static final String TOPIC_ID = "topicId";
  public static final String TOPIC_TITLE = "topicTitle";
  public static final String TOPIC_USER = "topicUSER";
  public static final String TOPIC_FLOOR = "topicFloor";
  public static final String EXTRA_REPLY_ID = "EXTRA_REPLY_ID";

  private Unbinder mUnbinder;
  private int topic_id;
  private int floor;
  private int mReplyId;
  private String topic_title;
  private String username;
  private MaterialDialog mDialog;

  @BindView(R.id.title) TextView title;
  @BindView(R.id.body) EditText body;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.btn_delete) TextView mBtnDelete;
  @BindColor(R.color.color_4d4d4d) int color_4d4d4d;
  @BindColor(R.color.color_999999) int color_999999;

  @Inject CreateReplyPresenter presenter;

  public static Intent getCallingIntent(Context context, int id, String title, String username, int replyFloor) {
    Intent callingIntent = new Intent(context, CreateReplyActivity.class);
    callingIntent.putExtra(TOPIC_ID, id);
    callingIntent.putExtra(TOPIC_TITLE, title);
    callingIntent.putExtra(TOPIC_USER, username);
    callingIntent.putExtra(TOPIC_FLOOR, replyFloor);
    return callingIntent;
  }

  public static Intent callingIntent(Context context, int id, String title) {
    Intent callingIntent = new Intent(context, CreateReplyActivity.class);
    callingIntent.putExtra(TOPIC_ID, id);
    callingIntent.putExtra(TOPIC_TITLE, title);
    return callingIntent;
  }

  public static Intent callingEditIntent(Context context, int id, String title) {
    Intent callingIntent = new Intent(context, CreateReplyActivity.class);
    callingIntent.putExtra(EXTRA_REPLY_ID, id);
    callingIntent.putExtra(TOPIC_TITLE, title);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reply_create);
    mUnbinder = ButterKnife.bind(this);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new CreateReplyModule(this))
        .inject(this);

    toolbar.setTitle(R.string.topic);
    toolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(toolbar);

    mDialog = new MaterialDialog.Builder(this).content(R.string.please_wait).progress(true, 0).build();

    topic_id = getIntent().getIntExtra(TOPIC_ID, 0);
    topic_title = getIntent().getStringExtra(TOPIC_TITLE);
    floor = getIntent().getIntExtra(TOPIC_FLOOR, 0);
    username = getIntent().getStringExtra(TOPIC_USER);

    title.setText(topic_title);
    mReplyId = getIntent().getIntExtra(EXTRA_REPLY_ID, 0);
    if (mReplyId != 0) {
      presenter.getTopicReply(mReplyId);
      mBtnDelete.setVisibility(View.VISIBLE);
    }
    if (floor != 0 && !TextUtils.isEmpty(username)) {
      String text = MessageFormat.format(getString(R.string.reply_prefix), floor, username) + " ";
      body.setText(text);
      body.setSelection(text.length());
    }

  }

  @OnClick(R.id.btn_delete)
  void onClickDelete() {
    new MaterialDialog.Builder(this)
        .content(R.string.delete_hint)
        .contentColor(color_4d4d4d)
        .positiveText(R.string.confirm)
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            presenter.deleteTopicReply(mReplyId);
          }
        })
        .negativeText(R.string.cancel)
        .negativeColor(color_999999)
        .show();
  }


  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_create_topic_reply, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        break;
      case R.id.action_send:
        if (TextUtils.isEmpty(body.getText().toString().trim())) {
          ToastUtils.showShort("评论内容不能为空");
          return false;
        }
        if (mReplyId != 0) {
          presenter.updateTopicReply(mReplyId, body.getText().toString().trim());
        } else {
          presenter.createTopicReply(topic_id, body.getText().toString().trim());
        }
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void showLoading() {
    mDialog.show();
  }

  @Override
  public void hideLoading() {
    mDialog.cancel();
  }

  @Override
  public void onGetReply(String bodyhtml) {
    body.setText(HtmlUtil.removeP(bodyhtml));
    body.setSelection(bodyhtml.length());
  }

  @Override
  public void finishActivity() {
    finish();
  }
  @Override
  public void onBackPressed() {
    new MaterialDialog.Builder(this)
        .content("退出此次编辑？")
        .contentColor(color_4d4d4d)
        .positiveText(R.string.confirm)
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            CreateReplyActivity.super.onBackPressed();
          }
        })
        .negativeText(R.string.cancel)
        .negativeColor(color_999999)
        .show();
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
