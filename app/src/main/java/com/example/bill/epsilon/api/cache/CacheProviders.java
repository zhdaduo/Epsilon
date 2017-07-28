package com.example.bill.epsilon.api.cache;

import com.example.bill.epsilon.bean.news.News;
import com.example.bill.epsilon.bean.newsnode.NewsNode;
import com.example.bill.epsilon.bean.notification.Notification;
import com.example.bill.epsilon.bean.site.Site;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.bean.topic.TopicReply;
import com.example.bill.epsilon.bean.topicnode.Node;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.bean.user.UserInfo;
import io.rx_cache.DynamicKey;
import io.rx_cache.DynamicKeyGroup;
import io.rx_cache.EvictProvider;
import io.rx_cache.LifeCache;
import io.rx_cache.Reply;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;

/**
 * Created by Bill on 2017/7/27.
 */

public interface CacheProviders {

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<Topic>>> getTopics(Observable<List<Topic>> topics, DynamicKeyGroup nodeIdAndOffset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<Topic>>> getUserCreateTopics(Observable<List<Topic>> topics, DynamicKey offset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<Topic>>> getUserFavorites(Observable<List<Topic>> topics, DynamicKey offset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<TopicDetail>> getTopicDetail(Observable<TopicDetail> topic, DynamicKey id, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<UserDetailInfo>> getUserInfo(Observable<UserDetailInfo> user, DynamicKey id, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<News>>> getNews(Observable<List<News>> news, DynamicKeyGroup nodeIdAndOffset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<Notification>>> getNotifications (Observable<List<Notification>> notifications, DynamicKey offset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<Site>>> getSite(Observable<List<Site>> sites, DynamicKey offset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<UserInfo>>> getUserFollowing(Observable<List<UserInfo>> users, DynamicKeyGroup usernameAndOffset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<com.example.bill.epsilon.bean.topic.Reply>>> getUserReplies(Observable<List<com.example.bill.epsilon.bean.topic.Reply>> replies, DynamicKeyGroup usernameAndOffset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<TopicReply>>> getReplies(Observable<List<TopicReply>> replies, DynamicKeyGroup idAndOffset, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<Node>>> readNodes(Observable<List<Node>> nodes, EvictProvider evictProvider);

  @LifeCache(duration = 7, timeUnit = TimeUnit.DAYS)
  Observable<Reply<List<NewsNode>>> readNewsNodes(Observable<List<NewsNode>> nodes, EvictProvider evictProvider);
}
