package com.example.bill.epsilon.ui.site;

import com.example.bill.epsilon.internal.di.scope.PerFragment;
import dagger.Subcomponent;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
@Subcomponent(modules = SiteModule.class)
public interface SiteComponent {

  void inject(SiteFragment fragment);
}
