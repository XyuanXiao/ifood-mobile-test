package com.ifoodchallenge.xyuan.xyuanifoodchallenge.presenter

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.EditorInfo
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract.SearchTweetsContract
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.SearchTweetsModel
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SearchTweetsPresenter(
    private val model: SearchTweetsModel
) : SearchTweetsContract.Presenter {

  private val TWEETS_LIST_KEY = "tweets_list_key"

  private lateinit var view: SearchTweetsContract.View
  private var tweetsList = ArrayList<Tweet>()
  private var userName = ""
  private var selectedTweetText = ""
  private var subscriptions = CompositeDisposable()
  private var searchTweetsInProgress = false


  override fun bindView(view: SearchTweetsContract.View) {
    this.view = view
  }

  override fun onViewCreated(savedInstanceState: Bundle?, extras: Bundle?) {
    val bundle = savedInstanceState ?: extras
    bundle?.let {
      val tweets: ArrayList<Tweet>? = it.getParcelableArrayList(TWEETS_LIST_KEY)
      tweets?.let { list ->
        tweetsList = list
        if (!tweetsList.isEmpty()) {
          searchSuccessLayout()
          view.updateTweetsList(tweetsList)
          view.toggleTweetsList(true)
//        subscriptions.add(checkTweetFeeling(selectedTweetText))
        }
      }
    }
  }

  override fun onViewDestroyed() {
    subscriptions.clear()
  }

  override fun onSaveInstanceState(bundle: Bundle?) {
    bundle?.putParcelableArrayList(TWEETS_LIST_KEY, tweetsList)
  }

  override fun onSearchFABPressed() {
    searchBarAvailable()
  }

  override fun onSearchMenuPressed(): Boolean {
    if (!searchTweetsInProgress) {
      searchBarAvailable()
      return true
    }
    return false
  }

  override fun onSearchUserPressed(user: String, actionId: Int): Boolean {
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
      if (user.length > 2) {
        if (view.networkAvailable()) {
          userName = user
          subscriptions.add(getTwitterAuthToken())
          searchTweetsInProgress = true

          tweetsList.clear()
          view.clearTweetsList()

          view.hideKeyboard()
          searchProgressLayout()
        } else {
          Log.d(this.javaClass.simpleName, "No internet connection when trying to get tweets from user")
          view.snackNoInternet()
        }
      } else {
        Log.d(this.javaClass.simpleName, "User search must be more than 2 characters")
        view.snackInvalidUser()
      }
      return true
    }
    return false
  }

  override fun onTweetClicked(tweet: Tweet, position: Int) {
    if(tweet.state == Tweet.STATE_UNCHECKED) {
      selectedTweetText = tweet.text

      //TODO: Request to Google emotion api
      //I'm creating a mock function to show how the app would behave visually
      glassHalfFullSimulator(tweet, position)
    }
  }

  private fun glassHalfFullSimulator(tweet: Tweet, position: Int) {
    //Everything is awesoooooommmeeeee!!! \o/\o/\o/\o/
    tweet.state = Tweet.STATE_CHECKING
    view.updateTweet(position)

    val handler = Handler()
    handler.postDelayed({
      tweet.emotion = Tweet.FEELING_HAPPY
      tweet.state = Tweet.STATE_CHECKED
      view.updateTweet(position)
    }, 2000)
  }

  private fun getTwitterAuthToken(): Disposable {
    return model.getTwitterAuthToken().subscribe(
        { auth ->
          auth?.let {
            if (it.token_type == "bearer") {
              subscriptions.add(getTweetsFromApi(userName, it.access_token))
            }
          }
        },
        { error ->
          Log.d(this.javaClass.simpleName, error.message)
          searchErrorLayout(userName)
          view.snackSearchError()
        }
    )
  }

  private fun getTweetsFromApi(user: String, token: String): Disposable {
    return model.getTweetsFromUser(user, token).subscribe(
        { tweets ->
          searchSuccessLayout()
          tweetsList = ArrayList(tweets)
          view.updateTweetsList(tweetsList)
          view.toggleTweetsList(true)
        },
        { error ->
          searchErrorLayout(user)
          Log.e(this.javaClass.simpleName, error.message ?: "Error trying to get tweets from user")
        }
    )
  }

//  private fun checkTweetFeeling(text: String): Disposable {
//    return model.checkTweetFeeling(text).subscribe
//  }

  private fun searchBarAvailable() {
    view.toggleSearchFAB(false)
    view.toggleSearchView(true)
  }

  private fun searchBarHidden() {
    view.toggleSearchFAB(true)
    view.toggleSearchView(false)
  }

  private fun searchErrorLayout(user: String) {
    searchBarAvailable()
    view.toggleSearchProgress(false)
    view.toggleSearchError(true)
    view.updateErrorMessage(user)
  }

  private fun searchProgressLayout() {
    view.toggleSearchFAB(false)
    view.toggleSearchView(false)
    view.toggleSearchError(false)
    view.toggleSearchProgress(true)
    view.toggleTweetsList(false)
  }

  private fun searchSuccessLayout() {
    searchBarHidden()
    view.toggleSearchProgress(false)
  }
}
