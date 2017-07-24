package com.example.bill.epsilon.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.topic.Reply;
import com.example.bill.epsilon.util.GlideImageGetter;
import com.example.bill.epsilon.util.HtmlUtil;
import com.example.bill.epsilon.util.TimeUtil;
import com.example.bill.epsilon.util.html.HtmlUtils;
import java.util.List;

/**
 * Created by Bill on 2017/7/20.
 */

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {

  private List<Reply> list;
  private OnItemClickListener listener;
  private HtmlUtils.Callback mCallback;

  public ReplyAdapter(List<Reply> list,
      OnItemClickListener listener, HtmlUtils.Callback mCallback) {
    this.list = list;
    this.listener = listener;
    this.mCallback = mCallback;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_user_reply, parent, false);

    return new ViewHolder(root);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final Reply reply = list.get(position);
    holder.topicTitle.setText(reply.getTopicTitle());
    HtmlUtils.parseHtmlAndSetText(holder.topicTitle.getContext(), reply.getBodyHtml(), holder.body, mCallback);
    holder.time.setText(TimeUtil.formatTime(reply.getUpdatedAt()));
    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onItemClick(v, reply);
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

  static class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.topic_title) TextView topicTitle;
    @BindView(R.id.body) TextView body;
    @BindView(R.id.time) TextView time;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  public interface OnItemClickListener {

    void onItemClick(View view, Reply reply);
  }
}
