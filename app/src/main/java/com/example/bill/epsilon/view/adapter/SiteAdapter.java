package com.example.bill.epsilon.view.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.site.Site;
import com.example.bill.epsilon.view.adapter.SiteBeanAdapter.OnItemClickListener;
import com.example.bill.epsilon.view.listener.ISiteListener;
import java.util.List;

/**
 * Created by Bill on 2017/7/16.
 */

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.SiteViewHolder> {

  private List<Site> list;
  private ISiteListener listener;

  public SiteAdapter(List<Site> list, ISiteListener listener) {
    this.list = list;
    this.listener = listener;
  }

  @Override
  public SiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_site, parent, false);

    return new SiteViewHolder(root);
  }

  @Override
  public void onBindViewHolder(SiteViewHolder holder, int position) {
    final Site site = list.get(position);
    holder.mTvName.setText(site.getName());
    GridLayoutManager manager = new GridLayoutManager(holder.mTvName.getContext(), 2);
    manager.setSpanSizeLookup(new SpanSizeLookup() {
      @Override
      public int getSpanSize(int position) {
        return (position == site.getSites().size()-1 && site.getSites().size() %2 == 1) ? 2:1;
      }
    });
    holder.mList.setLayoutManager(manager);
    SiteBeanAdapter siteBeanAdapter = new SiteBeanAdapter(site.getSites(), new OnItemClickListener() {
      @Override
      public void onItemClick(View view, String url) {
        if (listener != null) {
          listener.onItemCallBack(view, url);
        }
      }
    });
    holder.mList.setAdapter(siteBeanAdapter);
  }

  @Override
  public int getItemCount() {
    if (list == null || list.isEmpty()) {
      return 0;
    } else {
      return list.size();
    }
  }

  static class SiteViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.list) RecyclerView mList;

    public SiteViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
