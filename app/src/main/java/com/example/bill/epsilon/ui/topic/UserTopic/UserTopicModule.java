package com.example.bill.epsilon.ui.topic.UserTopic;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.ui.topic.UserTopic.UserTopicMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/18.
 */
@Module
public class UserTopicModule {

  UserTopicMVP.View view;

  public UserTopicModule(View view) {
    this.view = view;
  }

  @PerFragment
  @Provides
  TopicService provideTopicService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(TopicService.class);
  }

  @PerFragment
  @Provides
  UserService provideUserService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(UserService.class);
  }

  @PerFragment
  @Provides
  UserTopicMVP.Model provideModel(UserTopicModel model) {
    return model;
  }

  @PerFragment
  @Provides
  UserTopicMVP.Presenter providePresenter(UserTopicPresenter presenter) {
    return presenter;
  }

  @PerFragment
  @Provides
  UserTopicMVP.View provideView() {
    return view;
  }
}
