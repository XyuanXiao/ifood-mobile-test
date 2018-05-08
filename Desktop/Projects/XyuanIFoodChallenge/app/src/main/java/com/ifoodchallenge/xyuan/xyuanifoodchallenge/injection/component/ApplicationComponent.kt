package com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.component

import android.app.Application
import android.content.SharedPreferences
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.MyApp
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.api.TwitterApi
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.AppScope
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module.ApplicationModule
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module.NetworkModule
import dagger.Component

@AppScope
@Component(modules = arrayOf(
    ApplicationModule::class,
    NetworkModule::class)
)
interface ApplicationComponent {

  fun inject(myApp: MyApp)

  fun twitterApi(): TwitterApi
  fun application(): Application
  fun sharedPrefs(): SharedPreferences
}