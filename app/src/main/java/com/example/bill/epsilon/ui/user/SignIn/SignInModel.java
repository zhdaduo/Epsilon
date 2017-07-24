package com.example.bill.epsilon.ui.user.SignIn;

import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.bean.user.Token;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
public class SignInModel implements SignInMVP.Model {

  @Inject
  UserService service;

  @Inject
  public SignInModel() {
  }

  @Override
  public Observable<Token> getToken(String username, String password) {
    return service.getToken(Constant.VALUE_CLIENT_ID, Constant.VALUE_CLIENT_SECRET,
        Constant.VALUE_GRANT_TYPE_PASSWORD, username, password);
  }

  @Override
  public Observable<UserDetailInfo> Login(String username) {
    return service.getUser(username);
  }
}
