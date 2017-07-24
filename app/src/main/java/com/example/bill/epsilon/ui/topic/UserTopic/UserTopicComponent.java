package com.example.bill.epsilon.ui.topic.UserTopic;

import com.example.bill.epsilon.internal.di.scope.PerFragment;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/18.
 */
@PerFragment
@Subcomponent(modules = UserTopicModule.class)
public interface UserTopicComponent {

  void inject(UserTopicFragment fragment);
}
