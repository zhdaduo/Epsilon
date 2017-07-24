package com.example.bill.epsilon.ui.news.News;

import com.example.bill.epsilon.api.NewsService;
import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
public class NewsModel implements NewsMVP.Model {

  @Inject
  NewsService service;

  @Inject
  public NewsModel() {
  }

  @Override
  public Observable<List<News>> getNews(int offset) {
    return service.readNews(null, offset, Constant.PAGE_SIZE);
  }
}
