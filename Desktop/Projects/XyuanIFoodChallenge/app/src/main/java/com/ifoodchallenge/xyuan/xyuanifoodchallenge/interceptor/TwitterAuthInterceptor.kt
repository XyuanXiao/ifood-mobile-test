package com.ifoodchallenge.xyuan.xyuanifoodchallenge.interceptor

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.BuildConfig
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.Util


class TwitterAuthInterceptor : Interceptor  {

  override fun intercept(chain: Interceptor.Chain): Response {
    chain.request().let { oldRequest ->
      val newRequest = oldRequest.newBuilder()
      val identifier = oldRequest.header("Request_Identifier")
      val auth: String

      if (identifier != null) {
        auth = "Bearer $identifier"
        newRequest.apply {
          addHeader("Authorization", auth)
          addHeader("Content-Type", "application/json")
        }
      } else {
        auth = Credentials.basic(BuildConfig.TwitterKey, BuildConfig.TwitterSecret, Util.UTF_8)
        newRequest.apply {
          addHeader("Authorization", auth)
          addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
        }
      }

      return chain.proceed(newRequest.build())
    }
  }
}