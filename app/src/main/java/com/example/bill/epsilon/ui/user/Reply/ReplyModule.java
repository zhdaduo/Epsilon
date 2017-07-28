package com.example.bill.epsilon.ui.user.Reply;

import com.example.bill.epsilon.api.server.UserService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.user.Reply.ReplyMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/20.
 */
@Module
public class ReplyModule {

  ReplyMVP.View view;

  public ReplyModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  UserService provideTopicService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }

  @PerActivity
  @Provides
  ReplyMVP.Model provideModel(ReplyModel model) {
    return model;
  }

  @PerActivity
  @Provides
  ReplyMVP.Presenter providePresenter(ReplyPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  ReplyMVP.View provideView() {
    return view;
  }
}
