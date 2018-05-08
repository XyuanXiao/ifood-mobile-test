package com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract

object SearchTweetsContract {

  interface Presenter : PresenterActivity<View> {
    fun onSearchFABPressed()
    fun onSearchMenuPressed(): Boolean
    fun onSearchUserPressed(actionId: Int): Boolean

  }

  interface View {
    fun toggleSearchFAB(visible: Boolean)
    fun toggleSearchView(visible: Boolean)
    fun toggleSearchProgress(visible: Boolean)
    fun toggleSearchError(visible: Boolean)
  }
}