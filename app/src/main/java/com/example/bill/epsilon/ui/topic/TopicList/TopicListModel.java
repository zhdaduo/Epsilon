package com.example.bill.epsilon.ui.topic.TopicList;

import com.example.bill.epsilon.api.TopicService;
import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.internal.di.scope.PerFragment;
import com.example.bill.epsilon.util.Constant;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscriber;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Bill on 2017/7/16.
 */
@PerFragment
public class TopicListModel implements TopicListMVP.Model {

  @Inject
  TopicService service;

  @Inject
  public TopicListModel() {
  }

  @Override
  public Observable<List<Topic>> getTopics(int offset) {
    Observable<List<Topic>> topics = service
        .getTopics(null, null, offset, Constant.PAGE_SIZE);
    return topics;
  }

  @Override
  public Observable<List<Topic>> getTopTopics() {
    return Observable.create(new Observable.OnSubscribe<List<Topic>>() {
      @Override
      public void call(final Subscriber<? super List<Topic>> subscriber) {

        Document doc = null;
        try {
          doc = Jsoup.connect("https://www.diycode.cc/").get();
        } catch (IOException e) {
          e.printStackTrace();
        }
        int size = doc.getElementsByClass("fa fa-thumb-tack").size();
              Elements elements = doc.getElementsByClass("panel-body");
              Elements topics = elements.get(0).children();
              List<Topic> topicList = new ArrayList<>();
              for (int i = 0; i < size; i++) {
                Element topic = topics.get(i);
                Topic temp = new Topic();
                String href = topic.getElementsByClass("title media-heading")
                    .get(0)
                    .getElementsByTag("a")
                    .attr("href");
                temp.setId(Integer.valueOf(href.substring(href.lastIndexOf("/") + 1)));
                temp.setTitle(
                    topic.getElementsByClass("title media-heading").get(0).text());
                temp.setNodeName(topic.getElementsByClass("node").get(0).text());
                String time = topic.getElementsByClass("timeago").get(0).attr("title");
                StringBuilder sb = new StringBuilder(time);
                sb.insert(19, ".000");
                time = sb.toString();
                temp.setRepliesCount(Integer.valueOf(topic.getElementsByClass("count media-right").get(0).getElementsByTag("a").text()));
                temp.setLastReplyUserLogin(topic.getElementsByClass("hidden-mobile").get(0).getElementsByTag("a").text());
                temp.setRepliedAt(time);
                Topic.User user = new Topic.User();
                user.setAvatarUrl(topic.getElementsByTag("img").get(0).attr("src"));
                user.setLogin(topic.getElementsByClass("hacknews_clear").get(1).text());
                temp.setUser(user);
                temp.setPin(true);
                topicList.add(temp);
              }
              subscriber.onNext(topicList);
              subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<Ok> favoriteTopic(int id) {
    return service.favoriteTopic(id);
  }
}
