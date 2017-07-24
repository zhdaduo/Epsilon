package com.example.bill.epsilon.internal.di.component;

import android.content.Context;
import com.example.bill.epsilon.internal.di.module.ApplicationModule;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.ui.main.MainComponent;
import com.example.bill.epsilon.ui.main.MainModule;
import com.example.bill.epsilon.ui.news.CreateNews.CreateNewsComponent;
import com.example.bill.epsilon.ui.news.CreateNews.CreateNewsModule;
import com.example.bill.epsilon.ui.news.News.NewsComponent;
import com.example.bill.epsilon.ui.news.News.NewsModule;
import com.example.bill.epsilon.ui.notification.NotificationComponent;
import com.example.bill.epsilon.ui.notification.NotificationModule;
import com.example.bill.epsilon.ui.site.SiteComponent;
import com.example.bill.epsilon.ui.site.SiteModule;
import com.example.bill.epsilon.ui.topic.CreateReply.CreateReplyComponent;
import com.example.bill.epsilon.ui.topic.CreateReply.CreateReplyModule;
import com.example.bill.epsilon.ui.topic.CreateTopic.CreateTopicComponent;
import com.example.bill.epsilon.ui.topic.CreateTopic.CreateTopicModule;
import com.example.bill.epsilon.ui.topic.Topic.TopicComponent;
import com.example.bill.epsilon.ui.topic.Topic.TopicModule;
import com.example.bill.epsilon.ui.topic.TopicList.TopicListComponent;
import com.example.bill.epsilon.ui.topic.TopicList.TopicListModule;
import com.example.bill.epsilon.ui.topic.TopicReply.TopicReplyComponent;
import com.example.bill.epsilon.ui.topic.TopicReply.TopicReplyModule;
import com.example.bill.epsilon.ui.topic.UserTopic.UserTopicComponent;
import com.example.bill.epsilon.ui.topic.UserTopic.UserTopicModule;
import com.example.bill.epsilon.ui.user.Reply.ReplyComponent;
import com.example.bill.epsilon.ui.user.Reply.ReplyModule;
import com.example.bill.epsilon.ui.user.SignIn.SignInComponent;
import com.example.bill.epsilon.ui.user.SignIn.SignInModule;
import com.example.bill.epsilon.ui.user.User.UserComponent;
import com.example.bill.epsilon.ui.user.User.UserModule;
import com.example.bill.epsilon.ui.user.UserFollow.UserFollowComponent;
import com.example.bill.epsilon.ui.user.UserFollow.UserFollowModule;
import com.example.bill.epsilon.view.activity.AboutActivity;
import dagger.Component;
import javax.inject.Singleton;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

  Navigator getNavigator();
  Context context();


  TopicListComponent plus(TopicListModule module);
  SiteComponent plus(SiteModule module);
  NewsComponent plus(NewsModule module);
  UserTopicComponent plus(UserTopicModule module);
  UserComponent plus(UserModule module);
  SignInComponent plus(SignInModule module);
  MainComponent plus(MainModule module);
  UserFollowComponent plus(UserFollowModule module);
  TopicComponent plus(TopicModule module);
  NotificationComponent plus(NotificationModule module);
  CreateNewsComponent plus(CreateNewsModule module);
  ReplyComponent plus(ReplyModule module);
  TopicReplyComponent plus(TopicReplyModule module);
  CreateReplyComponent plus(CreateReplyModule module);
  CreateTopicComponent plus(CreateTopicModule module);
}
