package com.example.bill.epsilon.ui.site;

import com.example.bill.epsilon.bean.site.Site;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.SiteAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface SiteMVP {

  interface View extends IView {

    void setAdapter(SiteAdapter adapter);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();
  }

  interface Presenter extends IPresenter<View> {}

  interface Model {

    Observable<List<Site>> getSite(int offset);
  }
}
