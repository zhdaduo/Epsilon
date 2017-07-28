package com.example.bill.epsilon.ui.topic.CreateTopic;

import com.example.bill.epsilon.api.server.TopicNodeService;
import com.example.bill.epsilon.api.server.TopicService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.topic.CreateTopic.CreateTopicMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/20.
 */
@Module
public class CreateTopicModule {

  CreateTopicMVP.View view;

  public CreateTopicModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  TopicService provideTopicService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(TopicService.class);
  }

  @PerActivity
  @Provides
  TopicNodeService provideTopicNodeService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(TopicNodeService.class);
  }

  @PerActivity
  @Provides
  CreateTopicMVP.Model provideModel(CreateTopicModel model) {
    return model;
  }

  @PerActivity
  @Provides
  CreateTopicMVP.Presenter providePresenter(CreateTopicPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  CreateTopicMVP.View provideView() {
    return view;
  }
}
