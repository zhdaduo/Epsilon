package com.example.bill.epsilon.ui.user.User;

import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.user.User.UserMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/18.
 */
@Module
public class UserModule {

  UserMVP.View view;

  public UserModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  @Named("UserService")
  UserService provideSiteService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }

  @PerActivity
  @Provides
  @Named("UserServiceAuth")
  UserService provideSiteServiceAuth(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }

  @PerActivity
  @Provides
  UserMVP.Model provideModel(UserModel model) {
    return model;
  }

  @PerActivity
  @Provides
  UserMVP.Presenter providePresenter(UserPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  UserMVP.View provideView() {
    return view;
  }
}
