package com.example.bill.epsilon.view.adapter;

import android.support.v7.widget.RecyclerView;
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
import com.example.bill.epsilon.bean.user.UserInfo;
import com.example.bill.epsilon.view.listener.IUserListener;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Bill on 2017/7/19.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

  private List<UserInfo> list;
  private IUserListener listener;

  public UserAdapter(List<UserInfo> list,
      IUserListener listener) {
    this.list = list;
    this.listener = listener;
  }

  @Override
  public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View root = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_user, parent, false);

    return new UserViewHolder(root);
  }

  @Override
  public void onBindViewHolder(UserViewHolder holder, int position) {
    final UserInfo userInfo = list.get(position);
    holder.loginName.setText(userInfo.getLogin());
    Glide.with(holder.avatar.getContext())
        .load(userInfo.getAvatarUrl())
        .bitmapTransform(new CropCircleTransformation(holder.avatar.getContext()))
        .placeholder(R.drawable.shape_glide_img_error)
        .error(R.drawable.shape_glide_img_error)
        .crossFade()
        .into(holder.avatar);

    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) {
          listener.onItemClick(v, userInfo.getLogin());
        }
      }
    });

    holder.itemView.setOnLongClickListener(new OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        if (listener != null) {
          listener.onItemLongClick(v, userInfo);
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

  static class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar) ImageView avatar;
    @BindView(R.id.loginName) TextView loginName;

    public UserViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
