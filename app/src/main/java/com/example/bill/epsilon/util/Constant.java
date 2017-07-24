package com.example.bill.epsilon.util;

/**
 * Created by Bill on 2017/7/16.
 */

public class Constant {
  public static final String KEYSTORE_KEY_ALIAS = "DiyCode";
  public static final String VALUE_CLIENT_ID = "ef188c19";
  public static final String VALUE_CLIENT_SECRET =
      "074214e44a56cba526de60b3c2aff8b6afc6c1d5ebab0051450a8ff11bdb666d";
  public static final String VALUE_GRANT_TYPE_PASSWORD = "password";
  public static final String VALUE_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
  public static final String KEY_TOKEN = "Authorization";
  public static final String VALUE_TOKEN_PREFIX = "Bearer ";

  //UserTopicFragment
  public static final int TYPE_CREATE = 0;
  public static final int TYPE_FAVORITE = 1;

  //UserActivity
  public static final int USERActivity_Create = 1;
  public static final int USERActivity_Favorite = 2;
  public static final int USERActivity_Follow = 3;

  public static String VALUE_TOKEN = "";
  public static final int PAGE_SIZE = 20;
  public static class Token {
    public static final String SHARED_PREFERENCES_NAME = "sign";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String TOKEN_TYPE = "token_type";
    public static final String EXPIRES_IN = "expires_in";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String CREATED_AT = "created_at";
  }

  public static class User {
    public static final String LOGIN = "login";
    public static final String AVATAR_URL = "avatar_url";
    public static final String EMAIL = "email";
  }
}
