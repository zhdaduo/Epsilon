package com.example.bill.epsilon.ui.topic.TopicList;

import com.example.bill.epsilon.internal.di.scope.PerFragment;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
@Subcomponent(modules = TopicListModule.class)
public interface TopicListComponent {

  void inject(TopicListFragment fragment);
}
