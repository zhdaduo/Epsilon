package com.example.bill.epsilon.ui.news.CreateNews;

import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.bean.newsnode.NewsNode;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */

public interface CreateNewsMVP {

  interface View extends IView {

    void onGetNodes(List<NewsNode> nodes);

    void closeActivity();
  }

  interface Presenter extends IPresenter<View> {

  }

  interface Model {

    Observable<News> createNews(String title, String link, int nodeId);

    Observable<List<NewsNode>> getNewsNodes();
  }
}
