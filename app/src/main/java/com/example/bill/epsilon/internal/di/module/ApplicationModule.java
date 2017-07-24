package com.example.bill.epsilon.internal.di.module;

import android.app.Application;
import android.content.Context;
import com.example.bill.epsilon.AndroidApplication;
import com.example.bill.epsilon.navigation.Navigator;
import com.example.bill.epsilon.util.Constant;
import dagger.Module;
import dagger.Provides;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;
import javax.inject.Singleton;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.listener.ResponseErroListener;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

  private final AndroidApplication application;
  private String mBaseUrl;

  public ApplicationModule(AndroidApplication application, String mBaseUrl) {
    this.application = application;
    this.mBaseUrl = mBaseUrl;
  }

  @Singleton
  @Provides
  RxJavaCallAdapterFactory provideRxJavaCallAdapterFactory() {
    return RxJavaCallAdapterFactory.create();
  }

  @Singleton
  @Provides
  GsonConverterFactory provideGsonConverterFactory() {
    GsonConverterFactory factory = GsonConverterFactory.create();
    return factory;
  }

  @Singleton
  @Provides
  @Named("AuthClient")
  OkHttpClient provideOkHttpClientAuth() {
    return new OkHttpClient.Builder()
        .addInterceptor(new Interceptor() {
          @Override
          public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder();
            if (Constant.VALUE_TOKEN != null) {
              builder.addHeader(Constant.KEY_TOKEN,
                  Constant.VALUE_TOKEN_PREFIX + Constant.VALUE_TOKEN);
            }
            return chain.proceed(builder.build());
          }
        })
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build();
  }

  @Singleton
  @Provides
  @Named("Client")
  OkHttpClient provideOkHttpClient() {
    return new OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build();
  }

  @Singleton
  @Provides
  @Named("AuthRetrofit")
  Retrofit provideRetrofitAuth(@Named("AuthClient") OkHttpClient client,
      GsonConverterFactory converterFactory, RxJavaCallAdapterFactory adapterFactory) {
    return new Retrofit.Builder()
        .baseUrl(mBaseUrl)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(adapterFactory)
        .client(client)
        .build();
  }

  @Singleton
  @Provides
  @Named("Retrofit")
  Retrofit provideRetrofit(@Named("Client") OkHttpClient client,
      GsonConverterFactory converterFactory, RxJavaCallAdapterFactory adapterFactory) {
    return new Retrofit.Builder()
        .baseUrl(mBaseUrl)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(adapterFactory)
        .client(client)
        .build();
  }

  @Provides
  @Singleton
  Context provideApplicationContext() {
    return application;
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return application;
  }

  @Singleton
  @Provides
  RxErrorHandler provideRxErrorHandler(ResponseErroListener listener, Context context) {
    return RxErrorHandler
        .builder()
        .with(context)
        .responseErroListener(listener)
        .build();
  }

  @Singleton
  @Provides
  ResponseErroListener provideResponseErrorListener() {
    return ResponseErroListener.EMPTY;
  }

  @Singleton
  @Provides
  Navigator provideNavigator(Context context) {
    return new Navigator(context);
  }
}
