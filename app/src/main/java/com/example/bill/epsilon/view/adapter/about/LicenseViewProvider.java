package com.example.bill.epsilon.view.adapter.about;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.view.listener.IAboutListener;
import me.drakeet.multitype.ItemViewProvider;

/**
 * @author drakeet
 */
public class LicenseViewProvider extends ItemViewProvider<License, LicenseViewProvider.ViewHolder> {

  private final IAboutListener onLicenseListener;

  public LicenseViewProvider(IAboutListener onLicenseListener) {
    this.onLicenseListener = onLicenseListener;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
      @NonNull ViewGroup parent) {
    View root = inflater.inflate(R.layout.about_page_item_license, parent, false);
    return new ViewHolder(root);
  }

  @Override
  protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull License data) {

    holder.content.setText(data.name + " - " + data.author);
    holder.hint.setText(data.url + "\n" + data.type);
    holder.url = data.url;
    holder.itemView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        onLicenseListener.LicenseListener(v, holder.url);
      }
    });
  }

  static class ViewHolder extends RecyclerView.ViewHolder {

    TextView content;
    TextView hint;
    String url;

    ViewHolder(View itemView) {
      super(itemView);
      content = (TextView) itemView.findViewById(R.id.content);
      hint = (TextView) itemView.findViewById(R.id.hint);
      /*itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//          Intent intent = new Intent(v.getContext(), WebActivity.class);
//          intent.putExtra(WebActivity.URL, url);
//          v.getContext().startActivity(intent);
          //navigator.navigateToWebActivity(v.getContext(), url);
          onLicenseListener.LicenseListener();
        }
      });*/
    }
  }
}