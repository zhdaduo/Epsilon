package com.example.bill.epsilon.ui.news.CreateNews;

import com.example.bill.epsilon.internal.di.scope.PerActivity;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
@Subcomponent(modules = CreateNewsModule.class)
public interface CreateNewsComponent {

  void inject(CreateNewsActivity activity);
}
