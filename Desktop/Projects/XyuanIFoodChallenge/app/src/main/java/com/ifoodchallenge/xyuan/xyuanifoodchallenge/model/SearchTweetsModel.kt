package com.ifoodchallenge.xyuan.xyuanifoodchallenge.model

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.api.TwitterApi
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.SearchTweetsActivity

class SearchTweetsModel(activity: SearchTweetsActivity, twitterApi: TwitterApi) {
  private val context = activity
  private val api = twitterApi
}