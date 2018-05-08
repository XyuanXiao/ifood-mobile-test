package com.ifoodchallenge.xyuan.xyuanifoodchallenge.presenter

import android.os.Bundle
import android.os.Handler
import android.view.inputmethod.EditorInfo
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract.SearchTweetsContract
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.SearchTweetsModel
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet
import rx.Subscription
import rx.subscriptions.CompositeSubscription

class SearchTweetsPresenter(
    private val model: SearchTweetsModel
) : SearchTweetsContract.Presenter {

  private lateinit var view: SearchTweetsContract.View
  private lateinit var tweetsList: ArrayList<Tweet>
  private var subscriptions = CompositeSubscription()

  private var searchTweetsInProgress = false
  private var xyuan = false


  override fun bindView(view: SearchTweetsContract.View) {
    this.view = view
  }

  override fun onViewCreated(savedInstanceState: Bundle?, extras: Bundle?) {
    if (false) {
      //In case we have a users tweets saved, get them
    } else {

    }
//    subscriptions.add(onTweetPressed())
  }

//  private fun onTweetPressed(): Subscription {}

  override fun onSaveInstanceState(bundle: Bundle?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

  override fun onSearchUserPressed(actionId: Int): Boolean {
    searchTweetsInProgress = true

    (actionId == EditorInfo.IME_ACTION_SEARCH).let { isSearch ->
//      if (isSearch) subscriptions.add(getTweetsFromApi())
      searchProgressLayout()
      return isSearch
    }
  }

//  private fun getTweetsFromApi(): Subscription {
//    //Make TwitterApi request
//  }

  private fun waitAndGo() {
    val handler = Handler()
    handler.postDelayed(Runnable {
      when (xyuan) {
        true -> searchSuccessLayout()
        else -> searchErrorLayout()
      }
      searchTweetsInProgress = false
      xyuan = !xyuan
    }, 5000)
  }

  private fun searchBarAvailable() {
    view.toggleSearchFAB(false)
    view.toggleSearchView(true)
  }

  private fun searchBarHidden() {
    view.toggleSearchFAB(true)
    view.toggleSearchView(false)
  }

  private fun searchErrorLayout() {
    searchBarAvailable()
    view.toggleSearchProgress(false)
    view.toggleSearchError(true)
  }

  private fun searchProgressLayout() {
    view.toggleSearchFAB(false)
    view.toggleSearchView(false)
    view.toggleSearchError(false)
    view.toggleSearchProgress(true)

    waitAndGo()
  }

  private fun searchSuccessLayout() {
    searchBarHidden()
    view.toggleSearchProgress(false)
  }
}
