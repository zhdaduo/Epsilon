package com.example.bill.epsilon.ui.news.News;

import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.NewsAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface NewsMVP {

  interface View extends IView {

    void setAdapter(NewsAdapter adapter);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();

    void onCreateNewsRefresh();
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {

    Observable<List<News>> getNews(int offset, boolean update);
  }
}
