package com.example.bill.epsilon.ui.main;

import com.example.bill.epsilon.api.server.NotificationService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.main.MainMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/19.
 */
@Module
public class MainModule {

  MainMVP.View view;

  public MainModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  NotificationService provideTopicService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(NotificationService.class);
  }

  @PerActivity
  @Provides
  MainMVP.Model provideModel(MainModel model) {
    return model;
  }

  @PerActivity
  @Provides
  MainMVP.Presenter providePresenter(MainPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  MainMVP.View provideView() {
    return view;
  }
}
