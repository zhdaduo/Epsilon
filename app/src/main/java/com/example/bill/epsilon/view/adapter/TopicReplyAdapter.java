package com.example.bill.epsilon.view.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.util.TimeUtil;
import com.example.bill.epsilon.util.html.HtmlUtils;
import com.example.bill.epsilon.util.html.HtmlUtils.Callback;
import java.text.MessageFormat;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Bill on 2017/7/19.
 */

public class TopicReplyAdapter extends RecyclerView.Adapter<TopicReplyAdapter.ViewHolder> {

  private List<TopicReply> list;
  private OnItemClickListener listener;
  private HtmlUtils.Callback mCallback;

  public TopicReplyAdapter(List<TopicReply> list,
      OnItemClickListener listener, Callback mCallback) {
    this.list = list;
    this.listener = listener;
    this.mCallback = mCallback;
  }

  @Override
  public TopicReplyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_reply, parent, false);

    return new ViewHolder(root);
  }

  @Override
  public void onBindViewHolder(TopicReplyAdapter.ViewHolder holder, final int position) {
    final TopicReply topicReply = list.get(position);
    if (topicReply.isDeleted()) {
      holder.mLayout.setVisibility(View.GONE);
      holder.mTvHint.setVisibility(View.VISIBLE);
      holder.mTvHint.setText(
          MessageFormat.format(holder.itemView.getResources().getString(R.string.floor_deleted),
              position + 1));
      holder.mTvHint.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    } else {
      holder.mTvHint.setVisibility(View.GONE);
      holder.mLayout.setVisibility(View.VISIBLE);
      String avatarUrl = topicReply.getUser().getAvatarUrl();
      Glide.with(holder.mIvAvatar.getContext())
          .load(avatarUrl)
          .bitmapTransform(new CropCircleTransformation(holder.mIvAvatar.getContext()))
          .placeholder(R.drawable.shape_glide_img_error)
          .error(R.drawable.shape_glide_img_error)
          .crossFade()
          .into(holder.mIvAvatar);
      holder.mTvName.setText(topicReply.getUser().getLogin());
      holder.mTvFloor.setText(
          MessageFormat.format(holder.itemView.getResources().getString(R.string.floor), position + 1));
      holder.mTvTime.setText(TimeUtil.formatTime(topicReply.getUpdatedAt()));
      holder.mBtnEditReply.setVisibility(topicReply.getAbilities().isUpdate() ? View.VISIBLE : View.GONE);
      if (topicReply.getLikesCount() > 0) {
        holder.mTvLikeCount.setText(String.valueOf(topicReply.getLikesCount()));
      }
      HtmlUtils
          .parseHtmlAndSetText(holder.itemView.getContext(), topicReply.getBodyHtml(), holder.mTvContent, mCallback);

    }

    holder.mIvAvatar.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onUserClick(v, topicReply.getUser().getLogin());
      }
    });
    holder.mTvName.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onUserClick(v, topicReply.getUser().getLogin());
      }
    });
    holder.mBtnEditReply.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onEditReplyClick(v, topicReply);
      }
    });
    holder.mBtnLikeReply.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onLikeReplyClick(v, topicReply);
      }
    });
    holder.mBtnReply.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onReplyClick(v, topicReply, position+1);
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

  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.layout) LinearLayout mLayout;
    @BindView(R.id.iv_avatar) ImageView mIvAvatar;
    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.tv_floor) TextView mTvFloor;
    @BindView(R.id.tv_time) TextView mTvTime;
    @BindView(R.id.btn_edit_reply) ImageView mBtnEditReply;
    @BindView(R.id.btn_like_reply) ImageView mBtnLikeReply;
    @BindView(R.id.tv_like_count) TextView mTvLikeCount;
    @BindView(R.id.btn_reply) ImageView mBtnReply;
    @BindView(R.id.tv_content) TextView mTvContent;
    @BindView(R.id.tv_hint) TextView mTvHint;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
  public interface OnItemClickListener {

    void onUserClick(View view, String username);

    void onEditReplyClick(View view, TopicReply reply);

    void onLikeReplyClick(View view, TopicReply reply);

    void onReplyClick(View view, TopicReply reply, int floor);
  }
}
