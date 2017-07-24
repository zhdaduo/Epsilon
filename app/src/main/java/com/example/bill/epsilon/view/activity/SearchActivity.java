package com.example.bill.epsilon.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.example.bill.epsilon.view.widget.webview.DWebView;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Bill on 2017/7/22.
 */

public class SearchActivity extends BaseActivity {

  private Unbinder mUnbinder;

  @BindView(R.id.searchView)
  SearchView searchView;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.content)
  DWebView content;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  public static Intent getCallingIntent(Context context) {
    Intent callingIntent = new Intent(context, SearchActivity.class);
    callingIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    return callingIntent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_search);
    mUnbinder = ButterKnife.bind(this);
    super.onCreate(savedInstanceState);

    toolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(toolbar);
    setupSearchView();

    content.setWebViewClient(new CustomWebViewClient());
    content.getSettings().setJavaScriptEnabled(true);
    content.getSettings().setDomStorageEnabled(true);
    content.setOverScrollMode(WebView.OVER_SCROLL_NEVER);

    content.setVisibility(View.GONE);
    progressBar.setVisibility(View.GONE);
  }

  public void setupSearchView() {

    searchView.setIconifiedByDefault(true);
    searchView.onActionViewExpanded();
    searchView.requestFocus();
    searchView.setSubmitButtonEnabled(true);
    searchView.setFocusable(true);
    searchView.setIconified(false);
    searchView.requestFocusFromTouch();

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        progressBar.setVisibility(View.VISIBLE);
        content.loadUrl("https://www.diycode.cc/search?q=" + query);
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });
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

  private class CustomWebViewClient extends WebViewClient {

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      super.onPageStarted(view, url, favicon);
      view.setVisibility(WebView.INVISIBLE);
    }

    @Override
    public void onPageFinished(final WebView view, String url) {

      progressBar.setVisibility(View.GONE);
      view.setVisibility(WebView.VISIBLE);

      super.onPageFinished(view, url);
    }
  }
}
