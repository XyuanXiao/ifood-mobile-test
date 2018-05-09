package com.ifoodchallenge.xyuan.xyuanifoodchallenge.model

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.api.TwitterToken
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.api.TwitterApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SearchTweetsModel(
    private var twitterApi: TwitterApi
) {

  private val TWEETS_COUNT = "10"

  fun getTweetsFromUser(user: String, token: String): Observable<List<Tweet>> {
    return twitterApi.getTweetsFromUser(
        user,
        TWEETS_COUNT,
        token
    )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
  }

  fun getTwitterAuthToken(): Observable<TwitterToken> {
    return twitterApi.getAuthToken("client_credentials")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
  }

}