package com.example.bill.epsilon.view.adapter.about;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.view.adapter.about.LineViewBinder.ViewHolder;
import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Bill on 2017/6/27.
 */

public class LineViewBinder  extends ItemViewProvider<Line, ViewHolder> {

  @NonNull @Override
  protected ViewHolder onCreateViewHolder(
      @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
    View root = inflater.inflate(R.layout.about_page_item_line, parent, false);
    return new ViewHolder(root);
  }


  @Override
  protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Line data) {}


  static class ViewHolder extends RecyclerView.ViewHolder {

    ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
