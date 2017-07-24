package com.example.bill.epsilon.ui.user.Reply;

import com.example.bill.epsilon.bean.topic.Reply;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.ReplyAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */

public interface ReplyMVP {

  interface View extends IView {

    void setAdapter(ReplyAdapter adapter);

    void setEmpty(boolean isEmpty);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();
  }

  interface Presenter extends IPresenter<View> {

  }

  interface Model {
    Observable<List<Reply>> getUserReplies(String username, int offset);
  }
}
