package com.example.bill.epsilon.ui.news.CreateNews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
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
import com.example.bill.epsilon.bean.newsnode.NewsNode;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.view.widget.FlowLayout;
import com.example.bill.epsilon.view.widget.tag.TagAdapter;
import com.example.bill.epsilon.view.widget.tag.TagFlowLayout;
import com.example.bill.epsilon.view.widget.tag.TagFlowLayout.OnTagClickListener;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by Bill on 2017/7/20.
 */

public class CreateNewsActivity extends AppCompatActivity implements CreateNewsMVP.View {

  private MaterialDialog mDialog;
  private Unbinder mUnbinder;
  private int mNodeId;

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.title) TextInputLayout mTitle;
  @BindView(R.id.et_title) EditText mEtTitle;
  @BindView(R.id.link) TextInputLayout mLink;
  @BindView(R.id.et_link) EditText mEtLink;
  @BindView(R.id.flow_layout) TagFlowLayout mFlowLayout;

  @BindColor(R.color.color_4d4d4d) int color_4d4d4d;
  @BindColor(R.color.color_999999) int color_999999;

  @Inject CreateNewsPresenter presenter;

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, CreateNewsActivity.class);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_news);
    mUnbinder = ButterKnife.bind(this);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new CreateNewsModule(this))
        .inject(this);

    mToolbar.setNavigationIcon(R.drawable.ic_back);
    mToolbar.setTitle("创建新分享");
    setSupportActionBar(mToolbar);

    mDialog = new MaterialDialog.Builder(this).content(R.string.please_wait).progress(true, 0).build();

    Intent intent = getIntent();
    String action = intent.getAction();
    String type = intent.getType();
    if (action != null && action.equals(Intent.ACTION_SEND) && type != null) {
      if (type.equals("text/plain")) {
        String subject = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        mEtTitle.setText(subject);
        mEtLink.setText(text);
      }
    }

    String json = PrefUtil.getInstance(getApplicationContext(), Constant.Token.SHARED_PREFERENCES_NAME).getString("news_nodes", "");
    if (!TextUtils.isEmpty(json)) {
      GsonBuilder builder = new GsonBuilder();
      final List<NewsNode> list = builder.create().fromJson(json, new TypeToken<List<NewsNode>>() {
      }.getType());
      initTagLayout(list);
    } else {
      presenter.getNewsNodes();
    }
  }

  private void initTagLayout(final List<NewsNode> nodes) {
    TagAdapter adapter = new TagAdapter<NewsNode>(nodes) {
      @Override
      public View getView(FlowLayout parent, int position, NewsNode node) {
        TextView view = (TextView) View.inflate(CreateNewsActivity.this, R.layout.item_news_node, null);
        view.setText(node.getName());
        return view;
      }
    };
    mFlowLayout.setOnTagClickListener(new OnTagClickListener() {
      @Override
      public boolean onTagClick(View view, int position, FlowLayout parent) {
        mNodeId = nodes.get(position).getId();
        return true;
      }
    });
    mFlowLayout.setAdapter(adapter);
    adapter.setSelectedList(0);
    mNodeId = nodes.get(0).getId();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_toolbar_add_activity, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      onBackPressed();
    } else if (id == R.id.action_send) {
      String title = mEtTitle.getText().toString().trim();
      String link = mEtLink.getText().toString().trim();
      if (TextUtils.isEmpty(title)) {
        mTitle.setError("请输入标题");
      } else if (TextUtils.isEmpty(link)) {
        mTitle.setErrorEnabled(false);
        mLink.setError("请输入链接");
      } else if (!URLUtil.isNetworkUrl(link)) {
        mLink.setError("链接格式不正确");
      } else if (mNodeId == 0) {
        mTitle.setErrorEnabled(false);
        mLink.setErrorEnabled(false);
        ToastUtils.showShort("请选择分类");
      } else {
        mTitle.setErrorEnabled(false);
        mLink.setErrorEnabled(false);
        presenter.createNews(title, link, mNodeId);
      }
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
  public void showMessage(String message) {
    ToastUtils.showShort(message);
  }

  @Override
  public void onGetNodes(List<NewsNode> nodes) {
    initTagLayout(nodes);
  }

  @Override
  public void closeActivity() {
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
            CreateNewsActivity.super.onBackPressed();
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
    mUnbinder.unbind();
  }
}
