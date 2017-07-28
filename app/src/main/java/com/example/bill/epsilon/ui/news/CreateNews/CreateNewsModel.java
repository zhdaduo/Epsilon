package com.example.bill.epsilon.ui.news.CreateNews;

import com.example.bill.epsilon.api.server.NewsNodeService;
import com.example.bill.epsilon.api.server.NewsService;
import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.bean.newsnode.NewsNode;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class CreateNewsModel implements CreateNewsMVP.Model {

  @Inject
  NewsService newsService;
  @Inject
  NewsNodeService newsNodeService;

  @Inject
  public CreateNewsModel() {
  }

  @Override
  public Observable<News> createNews(String title, String link, int nodeId) {
    return newsService.createNews(Constant.VALUE_TOKEN_PREFIX + Constant.VALUE_TOKEN, title, link, nodeId);
  }

  @Override
  public Observable<List<NewsNode>> getNewsNodes() {
    return newsNodeService.readNewsNodes();
  }
}
