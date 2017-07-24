package com.example.bill.epsilon.ui.user.User;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/18.
 */
@PerActivity
@Subcomponent(modules = UserModule.class)
public interface UserComponent {

  void inject(UserActivity activity);
}
