package com.example.bill.epsilon.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.site.Site.Sites;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Bill on 2017/7/16.
 */

public class SiteBeanAdapter extends RecyclerView.Adapter<SiteBeanAdapter.SiteBeanViewHolder> {

  private List<Sites> list;
  private static OnItemClickListener listener;

  public SiteBeanAdapter(List<Sites> list, OnItemClickListener listener) {
    this.list = list;
    this.listener = listener;
  }

  @Override
  public SiteBeanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_sitebean, parent, false);

    return new SiteBeanViewHolder(root);
  }

  @Override
  public void onBindViewHolder(SiteBeanViewHolder holder, int position) {
    final Sites sites = list.get(position);
    holder.mTvName.setText(sites.getName());
    Glide.with(holder.mIvAvatar.getContext())
        .load(sites.getAvatarUrl())
        .bitmapTransform(new CropCircleTransformation(holder.mIvAvatar.getContext()))
        .error(R.drawable.shape_glide_img_error)
        .placeholder(R.drawable.shape_glide_img_error)
        .crossFade()
        .into(holder.mIvAvatar);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onItemClick(v, sites.getUrl());
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    if (list == null || list.isEmpty()) {
      return 0;
    } else {
      return list.size();
    }
  }

  static class SiteBeanViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;

    public SiteBeanViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public interface OnItemClickListener {

    void onItemClick(View view, String url);
  }
}
