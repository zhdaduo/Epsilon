package com.example.bill.epsilon.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.user.Token;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.news.News.NewsFragment;
import com.example.bill.epsilon.ui.site.SiteFragment;
import com.example.bill.epsilon.ui.topic.TopicList.TopicListFragment;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.view.adapter.ViewPagerAdapter;
import java.util.ArrayList;
import javax.inject.Inject;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
    implements MainMVP.View, NavigationView.OnNavigationItemSelectedListener {

  private Unbinder mUnbinder;
  private ImageView avatar;
  private TextView name;
  private UserDetailInfo userInfo;
  private boolean mHasNotification;
  private long mExitTime;
  private String username;

  @BindView(R.id.view_pager) ViewPager mViewPager;
  @BindView(R.id.tab_layout) TabLayout mTabLayout;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.drawer_layout) DrawerLayout drawer;
  @BindView(R.id.nav_view) NavigationView navigationView;
  @BindView(R.id.coordinator) CoordinatorLayout coordinator;
  @BindView(R.id.fab) FloatingActionButton fab;
  @Inject MainPresenter presenter;
  @Inject Navigator navigator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    mUnbinder = ButterKnife.bind(this);
    super.onCreate(savedInstanceState);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new MainModule(this))
        .inject(this);

    toolbar.setLogo(R.mipmap.logo_actionbar);
    toolbar.setNavigationIcon(R.drawable.ic_menu_arraw);
    toolbar.setTitle("");
    setSupportActionBar(toolbar);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    navigationView.setNavigationItemSelectedListener(this);
    View header = navigationView.getHeaderView(0);
    avatar = (ImageView) header.findViewById(R.id.imageView);
    name = (TextView) header.findViewById(R.id.loginName);

    final UserDetailInfo user = PrefUtil.getMe(MainActivity.this);
    username = user.getLogin();

    if (PrefUtil.getToken(this).getAccessToken() == null) {
      PrefUtil.clearMe(this);
      Glide.with(this)
          .load(R.mipmap.ic_avatar)
          .crossFade()
          .into(avatar);
      name.setText(getString(R.string.click_to_login));
    }

    avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!hasSignedIn()) return;
        navigator.navigateToUserActivity(username);
      }
    });

    setupViewPager();

    showUserInfo();

    presenter.getUnreadCount();
  }

  public void setupViewPager() {
    ArrayList<Fragment> fragments = new ArrayList<>();
    fragments.add(new TopicListFragment());
    fragments.add(new NewsFragment());
    fragments.add(new SiteFragment());
    String[] titles = new String[]{getString(R.string.topics), getString(R.string.news),
        getString(R.string.sites)};
    mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles));
    mViewPager.setOffscreenPageLimit(2);
    mTabLayout.setupWithViewPager(mViewPager);

    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset,
          int positionOffsetPixels) {
        if (position == 1) {
          fab.setScaleX(1 - positionOffset);
          fab.setScaleY(1 - positionOffset);
          fab.setAlpha(1 - positionOffset);
        } else if (position == 0 && fab.getAlpha() < 1) {
          fab.setScaleX(1 - positionOffset);
          fab.setScaleY(1 - positionOffset);
          fab.setAlpha(1 - positionOffset);
        }
      }

      @Override
      public void onPageSelected(int position) {
        if (position == 1) {
          fab.setAlpha(1f);
        }
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });
  }

  @OnClick(R.id.fab)
  void onFAB() {
    if (!hasSignedIn()) {
      return;
    }
    if (mViewPager.getCurrentItem() == 0) {
      navigator.navigateToCreateTopicActivity();
    } else if (mViewPager.getCurrentItem() == 1) {
      navigator.navigateToCreateNewsActivity();
    }
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      if ((System.currentTimeMillis() - mExitTime) > 2 * 1000) {
        Toast.makeText(getApplicationContext(),
            getString(R.string.exit), Toast.LENGTH_SHORT).show();
        mExitTime = System.currentTimeMillis();
      } else {
        super.onBackPressed();
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    if (mHasNotification) {
      menu.findItem(R.id.action_notification).setIcon(R.drawable.ic_notification_red);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      navigator.navigateToSettingActivity();
    } else if (id == R.id.action_notification){
      if (!hasSignedIn()) {
        return false;
      }
      navigator.navigateToNotificationActivity();
    } else if (id == R.id.action_search) {
      navigator.navigateToSearchActivity();
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.nav_post) {
      if (!hasSignedIn()) {
        return false;
      }
      navigator.navigateToUserCreateTopic(username);
    } else if (id == R.id.nav_collect) {
      if (!hasSignedIn()) {
        return false;
      }
      navigator.navigateToUserFavoriteTopic(username);
    } else if (id == R.id.nav_comment) {
      if (!hasSignedIn()) {
        return false;
      }
      navigator.navigateToReplyActivity(username);
    } else if (id == R.id.nav_about) {
      navigator.navigateToAboutActivity();
    } else if (id == R.id.nav_setting) {
      navigator.navigateToSettingActivity();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }

  @Override
  public void onLoginSuccess() {
    showUserInfo();
    presenter.getUnreadCount();
  }

  @Override protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (getResources().getString(R.string.logout_intent_action).equals(intent.getAction())) {
      PrefUtil.clearMe(this);
      Glide.with(this)
          .load(R.mipmap.ic_avatar)
          .crossFade()
          .into(avatar);
      name.setText(getString(R.string.click_to_login));
      mHasNotification = false;
      invalidateOptionsMenu();
    }
  }

  @Override
  public void onLogoutSuccess() {}

  @Override
  public void onGetNotificationUnread(boolean hasUnread) {
    mHasNotification = hasUnread;
    invalidateOptionsMenu();
  }

  public void showUserInfo() {
    Token token = PrefUtil.getToken(this);
    if (!TextUtils.isEmpty(token.getAccessToken())) {
      userInfo = PrefUtil.getMe(this);
      String login = userInfo.getLogin();
      String url = userInfo.getAvatarUrl();
      if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(url)) {
        name.setText(login);
        Glide.with(this)
            .load(url)
            .crossFade()
            .bitmapTransform(new CropCircleTransformation(this))
            .into(avatar);
      }
    }
  }

  private boolean hasSignedIn() {
    if (TextUtils.isEmpty(PrefUtil.getToken(this).getAccessToken())) {
      if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
      }
      Snackbar.make(coordinator, "请先登录", Snackbar.LENGTH_LONG)
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
    if (presenter != null) {
      presenter.onStart();
    }
  }

  @Override
  protected void onStop() {
    if (presenter != null) {
      presenter.onStop();
    }
    super.onStop();
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
