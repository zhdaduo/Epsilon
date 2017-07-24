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
import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.util.TimeUtil;
import com.example.bill.epsilon.util.UrlUtil;
import com.example.bill.epsilon.view.listener.INewsListener;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Bill on 2017/7/16.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

  private List<News> list;
  private static INewsListener listener;

  public NewsAdapter(List<News> list, INewsListener listener) {
    this.list = list;
    this.listener = listener;
  }

  @Override
  public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_news, parent, false);

    return new NewsViewHolder(root);
  }

  @Override
  public void onBindViewHolder(NewsViewHolder holder, int position) {
    final News news = list.get(position);
    holder.name.setText(news.getUser().getLogin());
    holder.topic.setText(news.getNodeName());
    holder.title.setText(news.getTitle());
    if (news.getRepliedAt() != null) {
      holder.time.setText(
          TimeUtil.computePastTime(news.getUpdatedAt()));
    } else {
      holder.time.setText(
          TimeUtil.computePastTime(news.getCreatedAt()));
    }
    holder.host.setText(UrlUtil.getHost(news.getAddress()));
    Glide.with(holder.avatar.getContext())
        .load(news.getUser().getAvatarUrl())
        .bitmapTransform(new CropCircleTransformation(holder.avatar.getContext()))
        .placeholder(R.drawable.shape_glide_img_error)
        .error(R.drawable.shape_glide_img_error)
        .crossFade()
        .into(holder.avatar);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onItemClick(v, news.getAddress());
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

  static class NewsViewHolder  extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.topic)
    TextView topic;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.host)
    TextView host;

    public NewsViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
