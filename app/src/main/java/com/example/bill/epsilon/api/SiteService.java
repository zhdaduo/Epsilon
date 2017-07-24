package com.example.bill.epsilon.api;

import com.example.bill.epsilon.bean.site.Site;
import java.util.List;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface SiteService {

  /**
   * 获取酷站信息
   */
  @GET("sites.json")
  Observable<List<Site>> getSite();
}
