package com.example.bill.epsilon.ui.topic.UserTopic;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.TopicListAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/18.
 */

public interface UserTopicMVP {

  interface View extends IView {

    void setAdapter(TopicListAdapter adapter);

    void setEmpty(boolean isEmpty);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();

    void onFavoriteSuccess();

    void onFavoriteRefresh();
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {

    Observable<List<Topic>> getUserTopics(String username, int offset);

    Observable<List<Topic>> getUserFavorites(String username, int offset);

    Observable<Ok> favoriteTopic(int id);

    Observable<Ok> unfavoriteTopic(int id);
  }
}
