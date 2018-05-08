package com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module

import android.app.Application
import android.content.Context
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.AppScope
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(application: Application) {

  private val SHARED_PREF_TWEETS = "tweets-pref"
  private val app = application

  @Provides
  @AppScope
  fun providesApplication() = app

  @Provides
  @AppScope
  fun providesSharedPreferences() = app
      .getSharedPreferences(SHARED_PREF_TWEETS, Context.MODE_PRIVATE)
}