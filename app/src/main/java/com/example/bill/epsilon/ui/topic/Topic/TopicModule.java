package com.example.bill.epsilon.ui.topic.Topic;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.topic.Topic.TopicMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/19.
 */
@Module
public class TopicModule {

  TopicMVP.View view;

  public TopicModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  TopicService provideTopicService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(TopicService.class);
  }

  @PerActivity
  @Provides
  TopicMVP.Model provideModel(TopicModel model) {
    return model;
  }

  @PerActivity
  @Provides
  TopicMVP.Presenter providePresenter(TopicPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  TopicMVP.View provideView() {
    return view;
  }
}
