package com.example.bill.epsilon.view.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by Bill on 2017/7/16.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

  private List<Fragment> mList;
  private CharSequence[] mTitles;

  public ViewPagerAdapter(FragmentManager fm, List<Fragment> mList, CharSequence[] mTitles) {
    super(fm);
    this.mList = mList;
    this.mTitles = mTitles;
  }

  @Override
  public Fragment getItem(int position) {
    return mList.get(position);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    if (mTitles != null) {
      return mTitles[position];
    }
    return super.getPageTitle(position);
  }

  @Override
  public int getCount() {
    return mList.size();
  }

  @Override
  public Parcelable saveState() {
    return null;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Fragment f = (Fragment) super.instantiateItem(container, position);
    View view = f.getView();
    if (view != null) {
      container.addView(view);
    }
    return f;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    View view = mList.get(position).getView();
    if (view != null) {
      container.removeView(view);
    }
  }
}
