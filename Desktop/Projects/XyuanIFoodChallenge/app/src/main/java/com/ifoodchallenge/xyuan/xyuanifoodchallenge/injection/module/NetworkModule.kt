package com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.api.TwitterApi
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.AppScope
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

@Module
class NetworkModule {

  private val OKHTTP_CACHE_SIZE: Long = 10 * 1024 * 1024
  private val BASE_URL = "https://api.twitter.com/1.1/search/"

  @Provides
  @AppScope
  fun providesGson() = GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()

  @Provides
  @AppScope
  fun providesGsonClient() = GsonConverterFactory
      .create()

  @Provides
  @AppScope
  fun providesOkHttpClient(cache: Cache) = OkHttpClient
      .Builder()
      .cache(cache)
      .build()

  @Provides
  @AppScope
  fun providesOkHttpCache(application: Application) = Cache(application.cacheDir, OKHTTP_CACHE_SIZE)

  @Provides
  @AppScope
  fun providesInterceptor() = HttpLoggingInterceptor()
      .setLevel(HttpLoggingInterceptor.Level.BODY)

  @Provides
  @AppScope
  fun providesTwitterApi(gson: Gson, okHttpClient: OkHttpClient) = Retrofit
      .Builder()
      .addConverterFactory(GsonConverterFactory.create(gson))
      .baseUrl(BASE_URL)
      .client(okHttpClient)
      .build()
      .create(TwitterApi::class.java)

  @Provides
  @AppScope
  fun providesRxAdapter() = RxJavaCallAdapterFactory
      .createWithScheduler(Schedulers.from(Executors.newCachedThreadPool()))
}