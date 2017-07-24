package com.example.bill.epsilon.ui.topic.CreateTopic;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
@Subcomponent(modules = CreateTopicModule.class)
public interface CreateTopicComponent {

  void inject(CreateTopicActivity activity);
}
