package com.example.bill.epsilon.ui.topic.TopicReply;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.bean.topic.Reply;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class TopicReplyModel implements TopicReplyMVP.Model {

  @Inject
  TopicService service;

  @Inject
  public TopicReplyModel() {
  }


  @Override
  public Observable<List<TopicReply>> getTopicReplies(int id, int offset) {
    return service.getReplies(id, offset, Constant.PAGE_SIZE);
  }
}
