package com.example.bill.epsilon.ui.topic.CreateTopic;

import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.bean.topicnode.Node;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */

public interface CreateTopicMVP {

  interface View extends IView {

    void onGetNodes(List<Node> sections);

    void showNewTopic(TopicDetail topicDetail);

    void finishActivity();
  }

  interface Presenter extends IPresenter<View> {

  }

  interface Model {

    Observable<TopicDetail> createTopic(String title, String body, int nodeId);

    Observable<List<Node>> getNodes();
  }
}
