package com.example.bill.epsilon.ui.user.UserFollow;

import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.ui.user.UserFollow.UserFollowMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/19.
 */
@Module
public class UserFollowModule {

  UserFollowMVP.View view;

  public UserFollowModule(View view) {
    this.view = view;
  }

  @PerFragment
  @Provides
  @Named("UserService")
  UserService provideUserService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }

  @PerFragment
  @Provides
  @Named("AuthUserService")
  UserService provideUserServiceAuth(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }

  @PerFragment
  @Provides
  UserFollowMVP.Model provideModel(UserFollowModel model) {
    return model;
  }

  @PerFragment
  @Provides
  UserFollowMVP.Presenter providePresenter(UserFollowPresenter presenter) {
    return presenter;
  }

  @PerFragment
  @Provides
  UserFollowMVP.View provideView() {
    return view;
  }
}
