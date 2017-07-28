package com.example.bill.epsilon.ui.topic.TopicReply;

import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.TopicReplyAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */

public interface TopicReplyMVP {

  interface View extends IView {

    void setAdapter(TopicReplyAdapter adapter);

    void setEmpty(boolean isEmpty);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();

    void onReplyRefresh();
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {
    Observable<List<TopicReply>> getTopicReplies(int id, int offset, boolean update);
  }
}
