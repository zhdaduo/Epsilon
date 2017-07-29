package com.example.bill.epsilon.ui.user.SignIn;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.bean.user.Token;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.ui.user.SignIn.SignInMVP.Model;
import com.example.bill.epsilon.ui.user.SignIn.SignInMVP.View;
import com.example.bill.epsilon.util.PrefUtil;
import com.example.bill.epsilon.util.RxUtil;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Bill on 2017/7/19.
 */
@PerActivity
public class SignInPresenter implements SignInMVP.Presenter {

  private SignInMVP.Model model;
  private SignInMVP.View view;
  private Application application;
  private RxErrorHandler mErrorHandler;



  @Inject
  public SignInPresenter(Model model,
      View view, Application application, RxErrorHandler mErrorHandler) {
    this.model = model;
    this.view = view;
    this.application = application;
    this.mErrorHandler = mErrorHandler;
  }


  public void validateCredentials(String username, String password) {
    if (TextUtils.isEmpty(username)) {
      view.setUsernameError();
    } else if (TextUtils.isEmpty(password)) {
      view.setPasswordError();
    } else {
      view.resetError();
      getToken(username, password);
    }
  }

  private void getToken(final String username, String password) {
        model.getToken(username, password)
            .subscribeOn(Schedulers.io())
            .retryWhen(new RetryWithDelay(3, 2))
            .doOnSubscribe(new Action0() {
              @Override
              public void call() {
                view.showLoading();
              }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .compose(RxUtil.<Token>bindToLifecycle(view))
            .subscribe(new ErrorHandleSubscriber<Token>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.hideLoading();
                view.loginFailed();
              }

              @Override
              public void onNext(@NonNull Token token) {
                PrefUtil.saveToken(application, token);
                Login(username);
              }
            });
  }

  private void Login(final String username) {
        model.Login(username)
            .compose(RxUtil.<UserDetailInfo>shortSchedulers())
            .doAfterTerminate(new Action0() {
              @Override
              public void call() {
                view.hideLoading();
              }
            })
            .compose(RxUtil.<UserDetailInfo>bindToLifecycle(view))
            .subscribe(new ErrorHandleSubscriber<UserDetailInfo>(mErrorHandler) {
              @Override
              public void onError(@NonNull Throwable e) {
                super.onError(e);
                view.loginFailed();
              }

              @Override
              public void onNext(@NonNull UserDetailInfo user) {
                PrefUtil.saveMe(application, user);
                view.loginSuccess(username);
              }
            });
  }

  @Override
  public void onDestroy() {
    view = null;
    mErrorHandler = null;
  }
}
