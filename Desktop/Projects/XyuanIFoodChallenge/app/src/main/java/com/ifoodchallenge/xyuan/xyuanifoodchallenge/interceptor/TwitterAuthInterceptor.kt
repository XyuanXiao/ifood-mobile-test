package com.ifoodchallenge.xyuan.xyuanifoodchallenge.interceptor

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class TwitterAuthInterceptor : Interceptor  {

  override fun intercept(chain: Interceptor.Chain): Response {
    val newRequest = chain.request().newBuilder()
    val auth = Credentials.basic(BuildConfig.TwitterKey, BuildConfig.TwitterSecret)
    newRequest.addHeader("Authorization", auth)

    return chain.proceed(newRequest.build())
  }
}