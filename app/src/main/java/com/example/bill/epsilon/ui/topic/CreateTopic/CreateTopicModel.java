package com.example.bill.epsilon.ui.topic.CreateTopic;

import com.example.bill.epsilon.api.TopicNodeService;
import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.bean.topicnode.Node;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class CreateTopicModel implements CreateTopicMVP.Model {

  @Inject
  TopicService topicService;
  @Inject
  TopicNodeService topicNodeService;

  @Inject
  public CreateTopicModel() {
  }


  @Override
  public Observable<TopicDetail> createTopic(String title, String body, int nodeId) {
    return topicService.newTopic(title, body, nodeId);
  }

  @Override
  public Observable<List<Node>> getNodes() {
    return topicNodeService.readNodes();
  }
}
