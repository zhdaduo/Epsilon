package com.example.bill.epsilon.ui.topic.CreateReply;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
@Subcomponent(modules = CreateReplyModule.class)
public interface CreateReplyComponent {

  void inject(CreateReplyActivity activity);
}
