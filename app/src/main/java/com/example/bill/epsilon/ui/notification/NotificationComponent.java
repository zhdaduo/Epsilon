package com.example.bill.epsilon.ui.notification;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
@Subcomponent(modules = NotificationModule.class)
public interface NotificationComponent {

  void inject(NotificationActivity activity);
}
