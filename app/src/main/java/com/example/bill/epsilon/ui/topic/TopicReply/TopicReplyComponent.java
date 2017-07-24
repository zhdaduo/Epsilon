package com.example.bill.epsilon.ui.topic.TopicReply;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
@Subcomponent(modules = TopicReplyModule.class)
public interface TopicReplyComponent {

  void inject(TopicReplyActivity avtivity);
}
