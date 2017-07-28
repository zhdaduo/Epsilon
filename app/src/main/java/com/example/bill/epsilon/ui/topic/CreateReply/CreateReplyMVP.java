package com.example.bill.epsilon.ui.topic.CreateReply;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */

public interface CreateReplyMVP {

  interface View extends IView{

    void onGetReply(String body);

    void finishActivity();
  }

  interface Presenter extends IPresenter<View> {

  }

  interface Model {

    Observable<TopicReply> createTopicReply(int topicId, String body);

    Observable<TopicReply> updateTopicReply(int id, String body);

    Observable<TopicReply> getTopicReply(int topicId);

    Observable<Ok> deleteTopicReply(int id);
  }
}
