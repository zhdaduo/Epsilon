package com.example.bill.epsilon.ui.site;

import com.example.bill.epsilon.api.server.SiteService;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.ui.site.SiteMVP.View;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.Retrofit;

/**
 * Created by Bill on 2017/7/16.
 */
@Module
public class SiteModule {

  SiteMVP.View view;

  public SiteModule(View view) {
    this.view = view;
  }

  @PerFragment
  @Provides
  SiteService provideSiteService(@Named("Retrofit") Retrofit retrofit) {
    return retrofit.create(SiteService.class);
  }

  @PerFragment
  @Provides
  SiteMVP.Model provideModel(SiteModel model) {
    return model;
  }

  @PerFragment
  @Provides
  SiteMVP.Presenter providePresenter(SitePresenter presenter) {
    return presenter;
  }

  @PerFragment
  @Provides
  SiteMVP.View provideView() {
    return view;
  }
}
