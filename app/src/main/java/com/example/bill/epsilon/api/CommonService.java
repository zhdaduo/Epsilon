package com.example.bill.epsilon.api;

import com.example.bill.epsilon.bean.base.Ok;
import retrofit2.http.DELETE;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Bill on 2017/7/23.
 */

public interface CommonService {
  /**
   * 删除设备
   */
  @DELETE("devices.json")
  Observable<Ok> deletedevice(@Query("platform") String platform,
      @Query("token") String token);
}
