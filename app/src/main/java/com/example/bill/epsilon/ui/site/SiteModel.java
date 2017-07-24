package com.example.bill.epsilon.ui.site;

import com.example.bill.epsilon.api.SiteService;
import com.example.bill.epsilon.bean.site.Site;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
public class SiteModel implements SiteMVP.Model {

  @Inject
  SiteService service;

  @Inject
  public SiteModel() {
  }

  @Override
  public Observable<List<Site>> getSite(int offset) {
    return service.getSite();
  }
}
