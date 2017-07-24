package com.example.bill.epsilon.api;

import com.example.bill.epsilon.bean.newsnode.NewsNode;
import java.util.List;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface NewsNodeService {
  /**
   * 获取节点列表
   */
  @GET("news/nodes.json")
  Observable<List<NewsNode>> readNewsNodes();
}
