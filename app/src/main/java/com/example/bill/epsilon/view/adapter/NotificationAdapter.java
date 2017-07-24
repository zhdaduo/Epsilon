package com.example.bill.epsilon.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.notification.Notification;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.util.TimeUtil;
import com.example.bill.epsilon.util.html.HtmlUtils;
import com.example.bill.epsilon.util.html.HtmlUtils.Callback;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Bill on 2017/7/20.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>  {

  private List<Notification> list;
  private OnItemLongClickListener longListener;
  private OnItemClickListener clickListener;
  private HtmlUtils.Callback mCallback;

  public NotificationAdapter(
      List<Notification> list, OnItemLongClickListener longListener,
      OnItemClickListener clickListener, HtmlUtils.Callback mCallback) {
    this.list = list;
    this.clickListener = clickListener;
    this.longListener = longListener;
    this.mCallback = mCallback;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_notification, parent, false);

    return new ViewHolder(root);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Notification data = list.get(position);
    Context context = holder.mIvAvatar.getContext();
    holder.mTvUnread.setVisibility(data.isRead() ? View.GONE : View.VISIBLE);
    if (data.getActor() == null && data.getMentionType() == null && data.getMention() == null
        && data.getTopic() == null && data.getReply() == null && data.getNode() == null) {
      holder.mLayout.setVisibility(View.GONE);
      holder.mTvHint.setVisibility(View.VISIBLE);
      holder.mTvHint.setText("相关信息已删除");
    } else {
      holder.mTvHint.setVisibility(View.GONE);
      holder.mLayout.setVisibility(View.VISIBLE);
      if (data.getActor() != null) {
        String avatarUrl = data.getActor().getAvatarUrl();
        Glide.with(context)
            .load(avatarUrl)
            .bitmapTransform(new CropCircleTransformation(context))
            .placeholder(R.drawable.shape_glide_img_error)
            .error(R.drawable.shape_glide_img_error)
            .crossFade()
            .into(holder.mIvAvatar);
        holder.mTvName.setText(data.getActor().getLogin());
      } else {
        UserDetailInfo user = PrefUtil.getMe(context);
        if (user != null) {
          String avatarUrl = user.getAvatarUrl();
          Glide.with(context)
              .load(avatarUrl)
              .bitmapTransform(new CropCircleTransformation(context))
              .placeholder(R.drawable.shape_glide_img_error)
              .error(R.drawable.shape_glide_img_error)
              .crossFade()
              .into(holder.mIvAvatar);
          holder.mTvName.setText(user.getLogin());
        }
      }
      holder.mTvTime.setText(TimeUtil.computePastTime(data.getCreatedAt()));
      String str_content = null;
      switch (data.getType()) {
        case "TopicReply":
          holder.mTvTitle.setText("在帖子\"" + data.getReply().getTopicTitle() + "\"回复了：");
          str_content = data.getReply().getBodyHtml();
          break;
        case "Mention":
          holder.mTvTitle.setText("提及你：");
          str_content = data.getMention().getBodyHtml();
          break;
        case "Topic":
          holder.mTvTitle.setText("创建了帖子：" + data.getTopic().getTitle());
          str_content = data.getTopic().getTitle();
          break;
        case "NodeChanged":
          holder.mTvTitle.setText("你发布的话题\"" + data.getTopic().getTitle() + "\"由于内容原因");
          str_content = "被管理员移到了\"" + data.getTopic().getNodeName() + "\"节点，请注意查看节点说明。";
          break;
        case "Hacknews":
          holder.mTvTitle.setText("你的分享");
          str_content = "暂无内容";
          break;
        default:
          break;
      }
      if (!TextUtils.isEmpty(str_content)) {
        HtmlUtils
            .parseHtmlAndSetText(context, str_content, holder.mTvContent,mCallback);
      }
      holder.mTvName.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          clickListener.onNameClick(v, data.getActor().getLogin());
        }
      });

      holder.mIvAvatar.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          clickListener.onNameClick(v, data.getActor().getLogin());
        }
      });

      holder.itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          clickListener.onItemClick(v, data);
        }
      });

      holder.itemView.setOnLongClickListener(new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
          longListener.onItemLongClick(v, data);
          return true;
        }
      });
    }
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
    @BindView(R.id.tv_time) TextView mTvTime;
    @BindView(R.id.tv_unread) TextView mTvUnread;
    @BindView(R.id.tv_title) TextView mTvTitle;
    @BindView(R.id.tv_content) TextView mTvContent;
    @BindView(R.id.tv_hint) TextView mTvHint;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
  public interface OnItemClickListener {

    void onItemClick(View view, Notification notification);

    void onNameClick(View view, String username);
  }

  public interface OnItemLongClickListener {

    void onItemLongClick(View view, Notification notification);
  }
}
