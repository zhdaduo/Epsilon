package com.example.bill.epsilon.ui.user.SignIn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.R;
import com.example.bill.epsilon.bean.event.LoginEvent;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.base.BaseActivity;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Bill on 2017/7/19.
 */

public class SignInActivity extends BaseActivity implements SignInMVP.View {

  private MaterialDialog mDialog;
  private Unbinder mUnbinder;

  @Inject SignInModel model;
  @Inject
  RxErrorHandler mErrorHandler;

  @BindView(R.id.name) EditText et_name;
  @BindView(R.id.password) EditText et_password;
  @BindView(R.id.sign_in) Button signIn;
  @BindView(R.id.sign_up) TextView signUp;
  @BindView(R.id.forget_password) TextView forgetPassword;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.name_til) TextInputLayout mName;
  @BindView(R.id.password_til) TextInputLayout mPassword;

  @Inject SignInPresenter presenter;
  @Inject Navigator navigator;

  public static Intent getCallingIntent(Context context) {
    Intent callingIntent = new Intent(context, SignInActivity.class);
    return callingIntent;
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    setContentView(R.layout.activity_sign_in);
    mUnbinder = ButterKnife.bind(this);
    super.onCreate(savedInstanceState);

    ((AndroidApplication) getApplication()).getApplicationComponent()
        .plus(new SignInModule(this))
        .inject(this);

    mDialog = new MaterialDialog.Builder(this).content(R.string.login_ing).progress(true, 0).build();
    toolbar.setNavigationIcon(R.drawable.ic_back);
    toolbar.setTitle(R.string.login);
    setSupportActionBar(toolbar);

  }

  @OnClick(R.id.sign_in)
  void SignIn() {
    String username = et_name.getText().toString().trim();
    String password = et_password.getText().toString().trim();
    presenter.validateCredentials(username, password);
  }

  @OnClick(R.id.sign_up)
  void onSignUp() {
    navigator.navigateToWebActivity("https://www.diycode.cc/account/sign_up");
  }

  @OnClick(R.id.forget_password)
  void onForgetPassword() {
    navigator.navigateToWebActivity("https://www.diycode.cc/account/password/new");
  }

  @Override
  public void showLoading() {
    mDialog.show();
  }

  @Override
  public void hideLoading() {
    mDialog.cancel();
  }

  @Override
  public void showMessage(String message) {
    ToastUtils.showShort(message);
  }

  @Override
  public void setUsernameError() {
    mName.setError(getString(R.string.please_input_username));
  }

  @Override
  public void setPasswordError() {
    mName.setErrorEnabled(false);
    mPassword.setError(getString(R.string.please_input_password));
  }

  @Override
  public void resetError() {
    mName.setErrorEnabled(false);
    mPassword.setErrorEnabled(false);
  }

  @Override
  public void loginSuccess(String username) {
    EventBus.getDefault().post(new LoginEvent());
    ToastUtils.showShort(getString(R.string.login_success));
    finish();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void loginFailed() {
    ToastUtils.showShort(getString(R.string.login_failed));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    presenter.onDestroy();
    if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
      mUnbinder.unbind();
    this.mUnbinder = null;
  }
}
