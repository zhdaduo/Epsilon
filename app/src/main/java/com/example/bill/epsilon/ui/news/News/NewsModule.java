package com.example.bill.epsilon.ui.news.News;

import com.example.bill.epsilon.api.NewsService;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.ui.news.News.NewsMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/16.
 */
@Module
public class NewsModule {

  NewsMVP.View view;

  public NewsModule(View view) {
    this.view = view;
  }

  @PerFragment
  @Provides
  NewsService provideSiteService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(NewsService.class);
  }

  @PerFragment
  @Provides
  NewsMVP.Model provideModel(NewsModel model) {
    return model;
  }

  @PerFragment
  @Provides
  NewsMVP.Presenter providePresenter(NewsPresenter presenter) {
    return presenter;
  }

  @PerFragment
  @Provides
  NewsMVP.View provideView() {
    return view;
  }
}
