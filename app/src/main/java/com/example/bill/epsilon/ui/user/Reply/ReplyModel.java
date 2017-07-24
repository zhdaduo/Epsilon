package com.example.bill.epsilon.ui.user.Reply;

import com.example.bill.epsilon.api.UserService;
import com.example.bill.epsilon.bean.topic.Reply;
import com.example.bill.epsilon.internal.di.scope.PerActivity;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

/**
 * Created by Bill on 2017/7/20.
 */
@PerActivity
public class ReplyModel implements ReplyMVP.Model {

  @Inject
  UserService service;

  @Inject
  public ReplyModel() {
  }

  @Override
  public Observable<List<Reply>> getUserReplies(String username, int offset) {
    return service.getUserReplies(username, offset, Constant.PAGE_SIZE);
  }
}
