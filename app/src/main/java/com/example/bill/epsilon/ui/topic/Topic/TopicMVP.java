package com.example.bill.epsilon.ui.topic.Topic;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Like;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */

public interface TopicMVP {

  interface View extends IView {

    void onGetTopicDetail(TopicDetail topic);

    void onFavoriteTopic();

    void onLikeTopic(Like like);

    void setLayout(boolean isNormal);
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {

    Observable<TopicDetail> getTopicDetail(int id);

    Observable<Like> likeTopic(String obj_type, Integer obj_id);

    Observable<Like> unlikeTopic(String obj_type, Integer obj_id);

    Observable<Ok> favoriteTopic(int id);

    Observable<Ok> unfavoriteTopic(int id);
  }
}
