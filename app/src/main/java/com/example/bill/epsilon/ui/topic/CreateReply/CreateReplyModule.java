package com.example.bill.epsilon.ui.topic.CreateReply;

import com.example.bill.epsilon.api.server.TopicService;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.topic.CreateReply.CreateReplyMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/20.
 */
@Module
public class CreateReplyModule {

  CreateReplyMVP.View view;

  public CreateReplyModule(View view) {
    this.view = view;
  }

  @PerActivity
  @Provides
  TopicService provideTopicService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(TopicService.class);
  }

  @PerActivity
  @Provides
  CreateReplyMVP.Model provideModel(CreateReplyModel model) {
    return model;
  }

  @PerActivity
  @Provides
  CreateReplyMVP.Presenter providePresenter(CreateReplyPresenter presenter) {
    return presenter;
  }

  @PerActivity
  @Provides
  CreateReplyMVP.View provideView() {
    return view;
  }
}
