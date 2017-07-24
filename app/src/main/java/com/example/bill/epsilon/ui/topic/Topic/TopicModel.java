package com.example.bill.epsilon.ui.topic.Topic;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Like;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
public class TopicModel implements TopicMVP.Model {

  @Inject
  TopicService service;

  @Inject
  public TopicModel() {
  }

  @Override
  public Observable<TopicDetail> getTopicDetail(int id) {
    return service.getTopic(id);
  }

  @Override
  public Observable<Like> likeTopic(String obj_type, Integer obj_id) {
    return service.like(obj_type, obj_id);
  }

  @Override
  public Observable<Like> unlikeTopic(String obj_type, Integer obj_id) {
    return service.unLike(obj_type, obj_id);
  }

  @Override
  public Observable<Ok> favoriteTopic(int id) {
    return service.favoriteTopic(id);
  }

  @Override
  public Observable<Ok> unfavoriteTopic(int id) {
    return service.unFavoriteTopic(id);
  }
}
