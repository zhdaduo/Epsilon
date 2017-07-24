package com.example.bill.epsilon.ui.topic.CreateTopic;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.bean.topicnode.Node;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.util.Constant;
import com.example.bill.epsilon.util.PrefUtil;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

/**
 * Created by Bill on 2017/7/20.
 */

public class CreateTopicActivity extends AppCompatActivity implements CreateTopicMVP.View {

  private Unbinder mUnbinder;
  private MaterialDialog mDialog;
  private List<Node> nodeList;
  private String[] sectionNames;
  private String[] nodeNames;

  @BindView(R.id.title) EditText title;
  @BindView(R.id.content) EditText content;
  @BindView(R.id.section_name) Spinner sectionName;
  @BindView(R.id.node_name) Spinner nodeName;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindColor(R.color.color_4d4d4d) int color_4d4d4d;
  @BindColor(R.color.color_999999) int color_999999;

  @Inject CreateTopicPresenter presenter;
  @Inject
  Navigator navigator;

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, CreateTopicActivity.class);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_new_topic);
    mUnbinder = ButterKnife.bind(this);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new CreateTopicModule(this))
        .inject(this);

    toolbar.setTitle(R.string.topic_new);
    toolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(toolbar);

    mDialog = new MaterialDialog.Builder(this).content(R.string.please_wait).progress(true, 0).build();

    String json = PrefUtil.getInstance(getApplicationContext(), Constant.Token.SHARED_PREFERENCES_NAME).getString("topic_nodes", "");
    if (!TextUtils.isEmpty(json)) {
      GsonBuilder builder = new GsonBuilder();
      final List<Node> list = builder.create().fromJson(json, new TypeToken<List<Node>>() {
      }.getType());
      onGetNodes(list);
    } else {
      presenter.getNodes();
    }
  }

  private void createTopic() {
    String section = sectionName.getDisplay().getName();
    int id = 45;
    for (Node node : nodeList) {
      if (node.getName().equals(section)) {
        id = node.getId();
      }
    }
    if (TextUtils.isEmpty(title.getText())) {
      ToastUtils.showShort("请输入标题");
      return;
    } else if (TextUtils.isEmpty(content.getText())) {
      ToastUtils.showShort("请输入发帖内容");
      return;
    }
    presenter.createTopic(title.getText().toString(), content.getText().toString(), id);
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

  }

  @Override
  public void onGetNodes(List<Node> sections) {
    if (sections == null || sections.isEmpty()) {
      return;
    }
    this.nodeList = sections;
    List<String> temp = getSectionNames(nodeList);
    sectionNames = temp.toArray(new String[temp.size()]);
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<String> adapter =
        new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sectionNames);
    // Specify the layout to use when the list of choices appears
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    sectionName.setAdapter(adapter);
    //nodeNames = nodeList.toArray(new String[nodeList.size()]);
    sectionName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String name = sectionNames[i];
        List<String> temp2 = getNodeNames(nodeList, name);
        nodeNames = temp2.toArray(new String[temp2.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateTopicActivity.this,
            android.R.layout.simple_spinner_item, nodeNames);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        nodeName.setAdapter(adapter);
      }

      @Override public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
  }

  private List<String> getSectionNames(List<Node> nodeList) {
    List<String> parents = new ArrayList<>();
    Set<String> set = new HashSet<>();
    for (Node node : nodeList) {
      String element = node.getSectionName();
      if (set.add(element)) parents.add(element);
    }
    return parents;
  }

  private List<String> getNodeNames(List<Node> nodeList, String sectionName) {
    List<String> nodeNameList = new ArrayList<>();
    for (Node node : nodeList) {
      String element = node.getSectionName();
      if (element.equals(sectionName)) {
        nodeNameList.add(node.getName());
      }
    }
    return nodeNameList;
  }
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_create_topic, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        onBackPressed();
        break;
      case R.id.action_send:
        createTopic();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void showNewTopic(TopicDetail topicDetail) {
    navigator.navigateToTopicActivity(topicDetail.getId());
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
            CreateTopicActivity.super.onBackPressed();
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
