package com.example.bill.epsilon.api.server;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Like;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.bean.topic.TopicDetail;
import com.example.bill.epsilon.bean.topic.TopicReply;
import java.util.List;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface TopicService {

  /**
   * 获取话题列表
   *
   * @param type 默认值 last_actived 范围 ["last_actived", "recent", "no_reply", "popular",
   * "excellent"]
   * @param nodeId 如果你需要只看某个节点的，请传此参数
   * @param offset 默认 0，从第 21 条开始就传 20
   * @param limit 默认 20 范围 [1..150]
   */
  @GET("topics.json")
  Observable<List<Topic>> getTopics(@Query("type") String type,
      @Query("node_id") Integer nodeId, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  /**
   * 获取话题详情
   *
   * @param id 帖子 id
   */
  @GET("topics/{id}.json") Observable<TopicDetail> getTopic(@Path("id") int id);

  /**
   * 获取话题评论
   *
   * @param id 帖子 id
   */
  @GET("topics/{id}/replies.json") Observable<List<TopicReply>> getReplies(@Path("id") int id,
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  /**
   * 创建话题
   *
   * @param title 话题标题
   * @param body 话题内容, Markdown 格式
   * @param nodeId 节点编号
   */
  @POST("topics.json") @FormUrlEncoded
  Observable<TopicDetail> newTopic(@Field("title") String title,
      @Field("body") String body, @Field("node_id") int nodeId);

  /**
   * 收藏话题
   */
  @POST("topics/{id}/favorite.json") Observable<Ok> favoriteTopic(@Path("id") int id);

  /**
   * 取消收藏话题
   */
  @POST("topics/{id}/unfavorite.json") Observable<Ok> unFavoriteTopic(@Path("id") int id);

  /**
   * 关注话题
   */
  @POST("topics/{id}/follow.json") Observable<Ok> followTopic(@Path("id") int id);

  /**
   * 取消关注话题
   */
  @POST("topics/{id}/unfollow.json") Observable<Ok> unFollowTopic(@Path("id") int id);

  /**
   * 赞
   *
   * @param obj_type ["topic", "reply", "news"]
   */
  @POST("likes.json") @FormUrlEncoded Observable<Like> like(@Field("obj_type") String obj_type,
      @Field("obj_id") Integer obj_id);

  /**
   * 取消赞
   *
   * @param obj_type ["topic", "reply", "news"]
   */
  @HTTP(method = "DELETE", path = "likes.json", hasBody = true) @FormUrlEncoded Observable<Like> unLike(
      @Field("obj_type") String obj_type, @Field("obj_id") Integer obj_id);

  /**
   * 创建回帖
   *
   * @param id 帖子 id
   * @param body 回帖内容, Markdown 格式
   */
  @POST("topics/{id}/replies.json") @FormUrlEncoded Observable<TopicReply> createReply(
      @Path("id") int id, @Field("body") String body);

  /**
   * 获取Topic回复
   */
  @GET("replies/{id}.json")
  Observable<TopicReply> getTopicReply(@Path("id") Integer id);

  /**
   * 更新Topic回复
   */
  @FormUrlEncoded
  @POST("replies/{id}.json")
  Observable<TopicReply> updateTopicReply(@Path("id") Integer id,
      @Field("body") String body);

  /**
   * 删除Topic回复
   */
  @DELETE("replies/{id}.json")
  Observable<Ok> deleteTopicReply(@Path("id") Integer id);


}
