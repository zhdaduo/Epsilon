package com.example.bill.epsilon.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.util.TimeUtil;
import com.example.bill.epsilon.view.listener.ITopicListListener;
import java.text.MessageFormat;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Bill on 2017/7/16.
 */

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.TopicViewHolder> {

  private List<Topic> list;
  private static ITopicListListener listListener;

  public TopicListAdapter(List<Topic> list,
      ITopicListListener listListener) {
    this.list = list;
    this.listListener = listListener;
  }

  @Override
  public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_topic, parent, false);

    return new TopicViewHolder(root);
  }

  @Override
  public void onBindViewHolder(TopicViewHolder holder, int position) {

    final Topic topic = list.get(position);
    holder.name.setText(topic.getUser().getLogin());
    holder.topic.setText(topic.getNodeName());
    holder.title.setText(topic.getTitle());
    if (topic.isPin()) {
      holder.pin.setVisibility(View.VISIBLE);
    } else {
      holder.pin.setVisibility(View.GONE);
    }

    if (topic.getRepliedAt() == null) {
      holder.time.setText(
          MessageFormat.format(holder.name.getResources().getString(R.string.publish_time),
              TimeUtil.computePastTime(topic.getCreatedAt())));
    } else {
      holder.time.setText(
          topic.getRepliesCount() + "条回复 • 最后由"
              + topic.getLastReplyUserLogin() +
              "于" + TimeUtil.computePastTime(topic.getRepliedAt()) + "回复");
    }

    Glide.with(holder.avatar.getContext())
        .load(topic.getUser().getAvatarUrl())
        .bitmapTransform(new CropCircleTransformation(holder.avatar.getContext()))
        .placeholder(R.drawable.shape_glide_img_error)
        .error(R.drawable.shape_glide_img_error)
        .crossFade()
        .into(holder.avatar);

    holder.avatar.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listListener.onUserItemClick(topic.getUser().getLogin());
      }
    });

    holder.name.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listListener.onUserItemClick(topic.getUser().getLogin());
      }
    });

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listListener != null) {
          listListener.onItemClick(v, topic.getId());
        }
      }
    });

    holder.itemView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (listListener != null) {
          listListener.onItemLongClick(v, topic);
        }
        return true;
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

  static class TopicViewHolder extends RecyclerView.ViewHolder {

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
    @BindView(R.id.pin)
    TextView pin;

    public TopicViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
