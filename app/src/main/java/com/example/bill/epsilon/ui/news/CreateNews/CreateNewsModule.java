package com.example.bill.epsilon.ui.news.CreateNews;

import com.example.bill.epsilon.api.server.NewsNodeService;
import com.example.bill.epsilon.api.server.NewsService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.news.CreateNews.CreateNewsMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/20.
 */
@Module
public class CreateNewsModule {

  CreateNewsMVP.View view;

  public CreateNewsModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  NewsService provideNewsService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(NewsService.class);
  }

  @PerActivity
  @Provides
  NewsNodeService provideNewsNodeService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(NewsNodeService.class);
  }

  @PerActivity
  @Provides
  CreateNewsMVP.Model provideModel(CreateNewsModel model) {
    return model;
  }

  @PerActivity
  @Provides
  CreateNewsMVP.Presenter providePresenter(CreateNewsPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  CreateNewsMVP.View provideView() {
    return view;
  }
}
