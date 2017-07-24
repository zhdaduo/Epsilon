package com.example.bill.epsilon.ui.user.Reply;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
@Subcomponent(modules = ReplyModule.class)
public interface ReplyComponent {

  void inject(ReplyActivity activity);
}
