package com.example.bill.epsilon.navigation;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.example.bill.epsilon.ui.news.CreateNews.CreateNewsActivity;
import com.example.bill.epsilon.ui.notification.NotificationActivity;
import com.example.bill.epsilon.ui.topic.CreateReply.CreateReplyActivity;
import com.example.bill.epsilon.ui.topic.CreateTopic.CreateTopicActivity;
import com.example.bill.epsilon.ui.topic.Topic.TopicActivity;
import com.example.bill.epsilon.ui.topic.TopicReply.TopicReplyActivity;
import com.example.bill.epsilon.ui.topic.UserTopic.UserCreateTopicActivity;
import com.example.bill.epsilon.ui.topic.UserTopic.UserFavoriteTopicActivity;
import com.example.bill.epsilon.ui.user.Reply.ReplyActivity;
import com.example.bill.epsilon.ui.user.SignIn.SignInActivity;
import com.example.bill.epsilon.ui.user.User.UserActivity;
import com.example.bill.epsilon.view.activity.AboutActivity;
import com.example.bill.epsilon.view.activity.ImageActivity;
import com.example.bill.epsilon.view.activity.SearchActivity;
import com.example.bill.epsilon.view.activity.SettingActivity;
import com.example.bill.epsilon.view.activity.WebActivity;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

  private Context context;

  @Inject
  public Navigator(Context context) {
    this.context = context;
  }

  public void navigateToBrower(String mUrl) {
    Intent intentToLaunch = new Intent(Intent.ACTION_VIEW);
    intentToLaunch.setData(Uri.parse(mUrl));
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void shareText(Context context, String title, String content) {
    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
    shareIntent.putExtra(Intent.EXTRA_TEXT, content);
    shareIntent.setType("text/plain");
    context.startActivity(Intent.createChooser(shareIntent, "分享到..."));
  }

  public void navigateToUserCreateTopic(String username) {
    if (context != null) {
      Intent intentToLaunch = UserCreateTopicActivity.getCallingIntent(context, username);
      intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToUserFavoriteTopic(String username) {
    if (context != null) {
      Intent intentToLaunch = UserFavoriteTopicActivity.getCallingIntent(context, username);
      intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToTopicActivity(int id) {
    if (context != null) {
      Intent intentToLaunch = TopicActivity.getCallingIntent(context, id);
      intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToNotificationActivity() {
    if (context != null) {
      Intent intentToLaunch = NotificationActivity.getCallingIntent(context);
      intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intentToLaunch);
    }
  }

  public void navigateToWebActivity(String url) {
    Intent intentToLaunch = WebActivity.getCallingIntent(context, url);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToAboutActivity() {
    Intent intentToLaunch = AboutActivity.getCallingIntent(context);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToReplyActivity(String username) {
    Intent intentToLaunch = ReplyActivity.getCallingIntent(context, username);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToImageActivity(String url) {
    Intent intentToLaunch = ImageActivity.getCallingIntent(context, url);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToUserActivity(String username) {
    Intent intentToLaunch = UserActivity.getCallingIntent(context, username);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToCreateReplyActivity(int id, String title, String username, int replyFloor) {
    Intent intentToLaunch = CreateReplyActivity.getCallingIntent(context, id, title, username, replyFloor);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToCreateReply(int id, String title) {
    Intent intentToLaunch = CreateReplyActivity.callingIntent(context, id, title);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToCreateReplyEdit(int id, String title) {
    Intent intentToLaunch = CreateReplyActivity.callingEditIntent(context, id, title);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToSignActivity() {
    Intent intentToLaunch = SignInActivity.getCallingIntent(context);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToCreateTopicActivity() {
    Intent intentToLaunch = CreateTopicActivity.getCallingIntent(context);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToCreateNewsActivity() {
    Intent intentToLaunch = CreateNewsActivity.getCallingIntent(context);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToTopicReplyActivity(int id, String title) {
    Intent intentToLaunch = TopicReplyActivity.getCallingIntent(context, id, title);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToSearchActivity() {
    Intent intentToLaunch = SearchActivity.getCallingIntent(context);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }

  public void navigateToSettingActivity() {
    Intent intentToLaunch = SettingActivity.getCallingIntent(context);
    intentToLaunch.setFlags(FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intentToLaunch);
  }
}
