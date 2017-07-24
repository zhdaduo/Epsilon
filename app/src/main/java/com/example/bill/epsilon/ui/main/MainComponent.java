package com.example.bill.epsilon.ui.main;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
@Subcomponent(modules = MainModule.class)
public interface MainComponent {

  void inject(MainActivity activity);
}
