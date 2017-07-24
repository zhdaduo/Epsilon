package com.example.bill.epsilon.ui.user.UserFollow;

import com.example.bill.epsilon.internal.di.scope.PerFragment;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/19.
 */
@PerFragment
@Subcomponent(modules = UserFollowModule.class)
public interface UserFollowComponent {

  void inject(UserFollowFragment followFragment);
}
