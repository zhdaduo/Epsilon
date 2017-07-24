package com.example.bill.epsilon.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.ui.base.BaseActivity;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Created by Bill on 2017/7/16.
 */

public class ImageActivity extends BaseActivity {

  public static final String URL = "url";
  private Unbinder mUnbinder;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.progress_bar) ProgressBar progressBar;
  @BindView(R.id.image) PhotoView image;

  public static Intent getCallingIntent(Context context, String url) {
    Intent callingIntent = new Intent(context, ImageActivity.class);
    callingIntent.putExtra(URL, url);
    return callingIntent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_image);
    mUnbinder = ButterKnife.bind(this);
    super.onCreate(savedInstanceState);

    String url = getIntent().getStringExtra(URL);

    toolbar.setNavigationIcon(R.drawable.ic_back);
    setSupportActionBar(toolbar);

    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
        .listener(new RequestListener<String, GlideDrawable>() {
          @Override
          public boolean onException(Exception e, String model, Target<GlideDrawable> target,
              boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
          }

          @Override public boolean onResourceReady(GlideDrawable resource, String model,
              Target<GlideDrawable> target, boolean isFromMemoryCache,
              boolean isFirstResource) {
            progressBar.setVisibility(View.GONE);
            return false;
          }
        })
        .into(image);
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
