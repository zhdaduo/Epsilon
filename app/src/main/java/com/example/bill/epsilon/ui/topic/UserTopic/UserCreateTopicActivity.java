package com.example.bill.epsilon.ui.topic.UserTopic;

import static com.example.bill.epsilon.util.Constant.TYPE_CREATE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.ui.base.BaseActivity;

/**
 * Created by Bill on 2017/7/18.
 */

public class UserCreateTopicActivity extends BaseActivity {

  public static final String User = "user";

  private String username;
  private Unbinder mUnbinder;

  @BindView(R.id.toolbar) Toolbar mToolbar;

  public static Intent getCallingIntent(Context context, String username) {
    Intent callingIntent = new Intent(context, UserCreateTopicActivity.class);
    callingIntent.putExtra(User, username);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);
    mUnbinder = ButterKnife.bind(this);

    if (getIntent() != null) {
      username = getIntent().getStringExtra(User);
    }

    setSupportActionBar(mToolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeAsUpIndicator(R.drawable.ic_back);
    ab.setDisplayHomeAsUpEnabled(true);
    ab.setTitle(R.string.my_topic);

    UserTopicFragment fragment = (UserTopicFragment) getSupportFragmentManager()
        .findFragmentById(R.id.container);

    if (fragment == null) {
      fragment = UserTopicFragment.newInstance(username, TYPE_CREATE);
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      transaction.add(R.id.container, fragment);
      transaction.commit();
    }
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
  protected void onDestroy() {
    super.onDestroy();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
      mUnbinder.unbind();
    }
    this.mUnbinder = null;
  }
}
