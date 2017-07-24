package com.example.bill.epsilon.ui.user.SignIn;

import com.example.bill.epsilon.bean.user.Token;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.ui.base.IPresenter;
import com.example.bill.epsilon.ui.base.IView;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */

public interface SignInMVP {

  interface View extends IView {

    void setUsernameError();

    void setPasswordError();

    void resetError();

    void loginSuccess(String username);

    void loginFailed();
  }

  interface Presenter extends IPresenter<View> {}

  interface Model {

    Observable<Token> getToken(String username, String password);

    Observable<UserDetailInfo> Login(String username);
  }
}
