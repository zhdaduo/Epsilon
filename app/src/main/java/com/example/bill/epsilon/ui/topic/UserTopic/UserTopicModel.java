package com.example.bill.epsilon.ui.topic.UserTopic;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/18.
 */
@PerFragment
public class UserTopicModel implements UserTopicMVP.Model {

  @Inject
  TopicService topicService;
  @Inject
  UserService userService;

  @Inject
  public UserTopicModel() {
  }

  @Override
  public Observable<List<Topic>> getUserTopics(String username, int offset) {
    return userService.getUserCreateTopics(username, offset, Constant.PAGE_SIZE);
  }

  @Override
  public Observable<List<Topic>> getUserFavorites(String username, int offset) {
    return userService.getUserFavoriteTopics(username, offset, Constant.PAGE_SIZE);
  }

  @Override
  public Observable<Ok> favoriteTopic(int id) {
    return topicService.favoriteTopic(id);
  }

  @Override
  public Observable<Ok> unfavoriteTopic(int id) {
    return topicService.unFavoriteTopic(id);
  }
}
