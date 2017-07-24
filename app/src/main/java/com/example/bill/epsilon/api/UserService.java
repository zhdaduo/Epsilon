package com.example.bill.epsilon.api;

import com.example.bill.epsilon.bean.base.Ok;
import com.example.bill.epsilon.bean.topic.Reply;
import com.example.bill.epsilon.bean.topic.Topic;
import com.example.bill.epsilon.bean.user.Token;
import com.example.bill.epsilon.bean.user.UserDetailInfo;
import com.example.bill.epsilon.bean.user.UserInfo;
import com.example.bill.epsilon.util.Constant;
import java.util.List;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Bill on 2017/7/16.
 */

public interface UserService {
  /**
   * 登录获取 Token
   */
  @POST("https://www.diycode.cc/oauth/token") @FormUrlEncoded
  Observable<Token> getToken(
      @Field("client_id") String client_id, @Field("client_secret") String client_secret,
      @Field("grant_type") String grant_type, @Field("username") String username,
      @Field("password") String password);

  /**
   * 获取新的 Token
   */
  @POST("https://www.diycode.cc/oauth/token") @FormUrlEncoded
  Observable<Token> refreshToken(
      @Field("client_id") String client_id, @Field("client_secret") String client_secret,
      @Field("grant_type") String grant_type, @Field("refresh_token") String refresh_token);

  /**
   * 获取当然登录者的资料
   *
   * @param token 当然登录者的 Token
   */
  @GET("users/me.json") Observable<UserDetailInfo> getMe(@Header(Constant.KEY_TOKEN) String token);

  /**
   * 获取用户详细资料
   *
   * @param loginName 用户登录名
   */
  @GET("users/{loginName}.json") Observable<UserDetailInfo> getUser(
      @Path("loginName") String loginName);

  /**
   * 获取用户屏蔽的用户
   *
   * @param loginName 用户登录名
   */
  @GET("users/{loginName}/blocked.json") Observable<List<UserInfo>> getUserBlocked(
      @Header(Constant.KEY_TOKEN) String token, @Path("loginName") String loginName,
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  /**
   * 获取用户正在关注的人
   *
   * @param loginName 用户登录名
   */
  @GET("users/{loginName}/following.json") Observable<List<UserInfo>> getUserFollowing(
      @Header(Constant.KEY_TOKEN) String token, @Path("loginName") String loginName,
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  /**
   * 获取用户的关注者列表
   *
   * @param loginName 用户登录名
   */
  @GET("users/{loginName}/followers.json") Observable<List<UserInfo>> getUserFollowers(
      @Header(Constant.KEY_TOKEN) String token, @Path("loginName") String loginName,
      @Query("offset") Integer offset, @Query("limit") Integer limit);

  /**
   * 屏蔽用户
   */
  @POST("users/{loginName}/block.json") Observable<Ok> blockUser(
      @Header(Constant.KEY_TOKEN) String token, @Path("loginName") String loginName);

  /**
   * 取消屏蔽用户
   */
  @POST("users/{loginName}/unblock.json") Observable<Ok> unBlockUser(
      @Header(Constant.KEY_TOKEN) String token, @Path("loginName") String loginName);

  /**
   * 关注用户
   */
  @POST("users/{loginName}/follow.json") Observable<Ok> followUser(
      @Path("loginName") String loginName);

  /**
   * 取消关注用户
   */
  @POST("users/{loginName}/unfollow.json") Observable<Ok> unFollowUser(
      @Path("loginName") String loginName);

  /**
   * 获取用户创建的话题列表
   *
   * @param loginName 用户的登录名
   * @param offset 默认 0，从第 21 条开始就传 20
   * @param limit 默认 20 范围 [1..150]
   */
  @GET("users/{login}/topics.json") Observable<List<Topic>> getUserCreateTopics(
      @Path("login") String loginName, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  /**
   * 获取用户收藏的话题列表
   *
   * @param loginName 用户的登录名
   * @param offset 默认 0，从第 21 条开始就传 20
   * @param limit 默认 20 范围 [1..150]
   */
  @GET("users/{login}/favorites.json") Observable<List<Topic>> getUserFavoriteTopics(
      @Path("login") String loginName, @Query("offset") Integer offset,
      @Query("limit") Integer limit);

  /**
   * 获取用户创建的回帖列表
   *
   * @param loginName 用户的登录名
   * @param offset 默认 0，从第 21 条开始就传 20
   * @param limit 默认 20 范围 [1..150]
   */
  @GET("users/{login}/replies.json") Observable<List<Reply>> getUserReplies(
      @Path("login") String loginName, @Query("offset") Integer offset,
      @Query("limit") Integer limit);
}
