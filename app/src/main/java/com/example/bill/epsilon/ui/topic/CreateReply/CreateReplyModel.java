package com.example.bill.epsilon.ui.topic.CreateReply;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class CreateReplyModel implements CreateReplyMVP.Model {

  @Inject
  TopicService service;

  @Inject
  public CreateReplyModel() {
  }


  @Override
  public Observable<TopicReply> createTopicReply(int topicId, String body) {
    return service.createReply(topicId, body);
  }

  @Override
  public Observable<TopicReply> updateTopicReply(int id, String body) {
    return service.updateTopicReply(id, body);
  }

  @Override
  public Observable<TopicReply> getTopicReply(int topicId) {
    return service.getTopicReply(topicId);
  }

  @Override
  public Observable<Ok> deleteTopicReply(int id) {
    return service.deleteTopicReply(id);
  }
}
