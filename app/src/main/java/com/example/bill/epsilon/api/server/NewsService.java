package com.example.bill.epsilon.api.server;

import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface NewsService {
  /**
   * 获取 news 列表
   */
  @GET("news.json")
  Observable<List<News>> readNews(@Query("node_id") Integer nodeId,
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  /**
   * 创建 News
   */
  @POST("news.json") @FormUrlEncoded
  Observable<News> createNews(
      @Header(Constant.KEY_TOKEN) String token, @Field("title") String title,
      @Field("address") String address, @Field("node_id") Integer node_id);
}
