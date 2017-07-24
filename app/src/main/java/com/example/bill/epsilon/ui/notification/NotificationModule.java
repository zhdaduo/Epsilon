package com.example.bill.epsilon.ui.notification;

import com.example.bill.epsilon.api.NotificationService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.notification.NotificationMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/20.
 */
@Module
public class NotificationModule {

  NotificationMVP.View view;

  public NotificationModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  NotificationService provideSiteService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(NotificationService.class);
  }

  @PerActivity
  @Provides
  NotificationMVP.Model provideModel(NotificationModel model) {
    return model;
  }

  @PerActivity
  @Provides
  NotificationMVP.Presenter providePresenter(NotificationPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  NotificationMVP.View provideView() {
    return view;
  }
}
