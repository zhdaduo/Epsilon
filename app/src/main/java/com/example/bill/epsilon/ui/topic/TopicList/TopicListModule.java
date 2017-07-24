package com.example.bill.epsilon.ui.topic.TopicList;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.ui.topic.TopicList.TopicListMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/16.
 */
@Module
public class TopicListModule {

  TopicListMVP.View view;

  public TopicListModule(View view) {
    this.view = view;
  }

  @PerFragment
  @Provides
  TopicService provideTopicService(@Named("AuthRetrofit") Retrofit retrofit) {
    return retrofit.create(TopicService.class);
  }

  @PerFragment
  @Provides
  TopicListMVP.Model provideModel(TopicListModel model) {
    return model;
  }

  @PerFragment
  @Provides
  TopicListMVP.Presenter providePresenter(TopicListPresenter presenter) {
    return presenter;
  }

  @PerFragment
  @Provides
  TopicListMVP.View provideView() {
    return view;
  }
}
