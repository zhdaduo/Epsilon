package com.example.bill.epsilon.ui.topic.Topic;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
@Subcomponent(modules = TopicModule.class)
public interface TopicComponent {

  void inject(TopicActivity activity);
}
