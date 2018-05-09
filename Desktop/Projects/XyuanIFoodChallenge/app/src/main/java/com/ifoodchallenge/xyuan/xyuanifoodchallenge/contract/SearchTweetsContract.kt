package com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.presenter.BasePresenter

object SearchTweetsContract {

  interface Presenter : BasePresenter<View> {
    fun onSearchFABPressed()
    fun onSearchMenuPressed(): Boolean
    fun onSearchUserPressed(user: String, actionId: Int): Boolean
    fun onTweetClicked(tweet: Tweet, position: Int)
  }

  interface View {
    fun toggleSearchFAB(visible: Boolean)
    fun toggleSearchView(visible: Boolean)
    fun toggleSearchProgress(visible: Boolean)
    fun toggleSearchError(visible: Boolean)
    fun toggleTweetsList(visible: Boolean)
    fun updateErrorMessage(user: String)
    fun updateTweetsList(tweetsList: List<Tweet>)
    fun updateTweet(position: Int)
    fun hideKeyboard()
    fun networkAvailable(): Boolean
    fun snackNoInternet()
    fun snackInvalidUser()
  }
}