package com.ifoodchallenge.xyuan.xyuanifoodchallenge.interceptor

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class TwitterAuthInterceptor : Interceptor  {

  private val TWITTER_CONSUMER_KEY = "pYziXVLsolxyF7xcUyTmIIIDE"
  private val TWITTER_CONSUMER_SECRET = "G64tUjP1VEzXwvdQScYJvjdJflheP0aVo696qTW3rLeSSlMPa3"

  override fun intercept(chain: Interceptor.Chain): Response {
    val newRequest = chain.request().newBuilder()
    val auth = Credentials.basic(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET)
    newRequest.addHeader("Authorization", auth)

    return chain.proceed(newRequest.build())
  }
}