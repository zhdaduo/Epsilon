package com.example.bill.epsilon.ui.topic.TopicReply;

import com.example.bill.epsilon.api.server.TopicService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.topic.TopicReply.TopicReplyMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/20.
 */
@Module
public class TopicReplyModule {

  TopicReplyMVP.View view;

  public TopicReplyModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  TopicService provideTopicService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(TopicService.class);
  }

  @PerActivity
  @Provides
  TopicReplyMVP.Model provideModel(TopicReplyModel model) {
    return model;
  }

  @PerActivity
  @Provides
  TopicReplyMVP.Presenter providePresenter(TopicReplyPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  TopicReplyMVP.View provideView() {
    return view;
  }
}
