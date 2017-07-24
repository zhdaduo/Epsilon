package com.example.bill.epsilon.ui.user.UserFollow;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.user.UserInfo;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import com.example.bill.epsilon.view.adapter.UserAdapter;
import java.util.List;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */

public interface UserFollowMVP {

  interface View extends IView {

    void setAdapter(UserAdapter adapter);

    void setEmpty(boolean isEmpty);

    void onLoadMoreComplete();

    void onLoadMoreError();

    void onLoadMoreEnd();

    void onFollowRefresh();
  }

  interface Presenter extends IPresenter<View> {

    void onStart();

    void onStop();
  }

  interface Model {

    Observable<List<UserInfo>> getFollowings(String username, int offset);

    Observable<Ok> unfollowUser(String username);

    Observable<Ok> followUser(String username);
  }
}
