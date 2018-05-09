package com.ifoodchallenge.xyuan.xyuanifoodchallenge.model

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.api.TwitterApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SearchTweetsModel(
    private var twitterApi: TwitterApi
) {

  fun getTweetsFromUser(user: String): Observable<List<Tweet>> {
    return twitterApi.getTweetsFromUser(
        user,
        "10"
    )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
  }

}