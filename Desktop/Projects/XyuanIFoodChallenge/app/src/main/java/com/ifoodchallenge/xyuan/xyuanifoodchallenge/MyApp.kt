package com.ifoodchallenge.xyuan.xyuanifoodchallenge

import android.app.Application
import android.content.Context
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.component.ApplicationComponent
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.component.DaggerApplicationComponent
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module.ApplicationModule

class MyApp : Application() {

  private lateinit var applicationComponent: ApplicationComponent

  override fun onCreate() {
    super.onCreate()
    initAppComponent()
  }

  private fun initAppComponent() {
    applicationComponent = DaggerApplicationComponent
        .builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }

  fun get(context: Context) = context.applicationContext as MyApp

  fun getApplicationComponent(): ApplicationComponent = applicationComponent
}