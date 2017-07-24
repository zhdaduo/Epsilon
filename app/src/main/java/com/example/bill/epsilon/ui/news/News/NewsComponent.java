package com.example.bill.epsilon.ui.news.News;

import com.example.bill.epsilon.internal.di.scope.PerFragment;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
@Subcomponent(modules = NewsModule.class)
public interface NewsComponent {

  void inject(NewsFragment fragment);
}
