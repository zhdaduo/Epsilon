package com.example.bill.epsilon.ui.user.SignIn;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
@Subcomponent(modules = SignInModule.class)
public interface SignInComponent {

  void inject(SignInActivity activity);
}
