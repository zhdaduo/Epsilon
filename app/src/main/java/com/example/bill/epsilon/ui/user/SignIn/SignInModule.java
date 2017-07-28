package com.example.bill.epsilon.ui.user.SignIn;

import com.example.bill.epsilon.api.server.UserService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.user.SignIn.SignInMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/19.
 */
@Module
public class SignInModule {

  SignInMVP.View view;

  public SignInModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  UserService provideSiteService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }

  @PerActivity
  @Provides
  SignInMVP.Model provideModel(SignInModel model) {
    return model;
  }

  @PerActivity
  @Provides
  SignInMVP.Presenter providePresenter(SignInPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  SignInMVP.View provideView() {
    return view;
  }
}
