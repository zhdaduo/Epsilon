package com.example.bill.epsilon.ui.topic.TopicList;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.UserTopicAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface TopicListMVP {

  interface View extends IView {

    void setAdapter(UserTopicAdapter adapter);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();

    void onFavoriteSuccess();

    void onCreteTopicRefresh();
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {

    Observable<List<Topic>> getTopics(int offset, boolean update);

    Observable<List<Topic>> getTopTopics();

    Observable<Ok> favoriteTopic(int id);
  }
}
