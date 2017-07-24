package com.example.bill.epsilon.api;

import com.example.bill.epsilon.bean.topicnode.Node;
import java.util.List;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface TopicNodeService {
  /**
   * 获取节点列表
   */
  @GET("nodes.json")
  Observable<List<Node>> readNodes();
}
