package com.example.bill.epsilon.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.BuildConfig;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.example.bill.epsilon.view.adapter.about.Card;
import com.example.bill.epsilon.view.adapter.about.CardViewProvider;
import com.example.bill.epsilon.view.adapter.about.Category;
import com.example.bill.epsilon.view.adapter.about.CategoryViewProvider;
import com.example.bill.epsilon.view.adapter.about.Contributor;
import com.example.bill.epsilon.view.adapter.about.ContributorViewProvider;
import com.example.bill.epsilon.view.adapter.about.License;
import com.example.bill.epsilon.view.adapter.about.LicenseViewProvider;
import com.example.bill.epsilon.view.adapter.about.Line;
import com.example.bill.epsilon.view.adapter.about.LineViewBinder;
import com.example.bill.epsilon.view.listener.IAboutListener;
import javax.inject.Inject;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Bill on 2017/7/20.
 */

public class AboutActivity extends BaseActivity {

  protected Toolbar toolbar;
  protected CollapsingToolbarLayout collapsingToolbar;

  protected Items items;
  protected MultiTypeAdapter adapter;
  protected TextView slogan, version;


  protected Navigator navigator;

  public static Intent getCallingIntent(Context context) {
    Intent callingIntent = new Intent(context, AboutActivity.class);
    return callingIntent;
  }

  protected void onCreateHeader(ImageView icon, TextView slogan, TextView version) {
    setHeaderContentColor(Color.parseColor("#999999"));
    setNavigationIcon(R.drawable.ic_menu_close);
    icon.setImageResource(R.mipmap.logo);
    slogan.setText("A third-party Android client of Diycode.");
    version.setText("version " + BuildConfig.VERSION_NAME);
  }

  protected void onItemsCreated(@NonNull Items items) {
    items.add(new Category("介绍"));
    items.add(new Card(getString(R.string.card_content), "DiyCode社区"));
    items.add(new Line());

    items.add(new Category("开发人员"));
    items.add(new Contributor(R.mipmap.ic_avatar_github, "YouTwo", "Android Developer",
        "https://github.com/zhdaduo"));
    items.add(new Line());

    items.add(new Category("使用的开源库"));
    items.add(new License("MultiType", "drakeet", License.APACHE_2,
        "https://github.com/drakeet/MultiType"));
    items.add(new License("about-page", "drakeet", License.APACHE_2,
        "https://github.com/drakeet/about-page"));
    items.add(new License("butterknife", "JakeWharton", License.APACHE_2,
        "https://github.com/JakeWharton/butterknife"));
    items.add(
        new License("gson", "google", License.APACHE_2, "https://github.com/google/gson"));
    items.add(new License("glide", "bumptech", License.APACHE_2,
        "https://github.com/bumptech/glide"));
    items.add(new License("retrofit", "square", License.APACHE_2,
        "https://github.com/square/retrofit"));
    items.add(new License("eventbus", "greenrobot", License.APACHE_2,
        "https://github.com/greenrobot/EventBus"));
    items.add(new License("PhotoView", "chrisbanes", License.APACHE_2,
        "https://github.com/chrisbanes/PhotoView"));
    items.add(new License("BGASwipeBackLayout-Android", "bingoogolapple", License.APACHE_2,
        "https://github.com/bingoogolapple/BGASwipeBackLayout-Android"));
    items.add(new License("StatusBarUtil", "laobie", License.APACHE_2,
        "https://github.com/laobie/StatusBarUtil"));
    items.add(new License("Dagger2", "google", License.APACHE_2,
        "https://github.com/google/dagger"));
  }

  @Nullable
  protected CharSequence onCreateTitle() {
    return null;
  }

  @SuppressWarnings("ConstantConditions") @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.about_page_main_activity);

    navigator = ((AndroidApplication)getApplicationContext()).getApplicationComponent().getNavigator();

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    ImageView icon = (ImageView) findViewById(R.id.icon);
    slogan = (TextView) findViewById(R.id.slogan);
    version = (TextView) findViewById(R.id.version);
    collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    onCreateHeader(icon, slogan, version);

    final CharSequence title = onCreateTitle();
    if (title != null) {
      collapsingToolbar.setTitle(title);
    }

    toolbar.setTitle("关于");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
    onSetupRecyclerView(recyclerView);
  }


  private void onSetupRecyclerView(RecyclerView recyclerView) {
    items = new Items();
    onItemsCreated(items);
    adapter = new MultiTypeAdapter(items);
    adapter.register(Category.class, new CategoryViewProvider());
    adapter.register(Card.class, new CardViewProvider(listener));
    adapter.register(Contributor.class, new ContributorViewProvider(listener));
    adapter.register(License.class, new LicenseViewProvider(listener));
    adapter.register(Line.class, new LineViewBinder());
    recyclerView.setAdapter(adapter);
  }

  /**
   * Set the header view background to a given resource and replace the default value
   * ?attr/colorPrimary.
   * The resource should refer to a Drawable object or 0 to remove the background.
   *
   * @param resId The identifier of the resource.
   */
  public void setHeaderBackgroundResource(@DrawableRes int resId) {
    if (collapsingToolbar == null) {
      collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    }
    collapsingToolbar.setContentScrimResource(resId);
    collapsingToolbar.setBackgroundResource(resId);
  }

  public void setHeaderContentColor(@ColorInt int color) {
    collapsingToolbar.setCollapsedTitleTextColor(color);
    slogan.setTextColor(color);
    version.setTextColor(color);
  }

  /**
   * Set the icon to use for the toolbar's navigation button.
   *
   * @param resId Resource ID of a drawable to set
   */
  public void setNavigationIcon(@DrawableRes int resId) {
    toolbar.setNavigationIcon(resId);
  }

  @Override public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(menuItem);
  }

  @Override public void setTitle(CharSequence title) {
    collapsingToolbar.setTitle(title);
  }

  /**
   * Listener for clicks on about screen.
   */
  IAboutListener listener = new IAboutListener() {
    @Override
    public void CardListener(View view) {
      navigator.navigateToWebActivity("https://www.diycode.cc");
    }

    @Override
    public void ContributorListener(View view, String url) {
      navigator.navigateToWebActivity(url);
    }

    @Override
    public void LicenseListener(View view, String url) {
      navigator.navigateToWebActivity(url);
    }
  };
}
