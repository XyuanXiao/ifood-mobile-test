package com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.MyApp
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.R
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract.SearchTweetsContract
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.extention.internetAvailable
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.component.DaggerSearchTweetsComponent
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module.SearchTweetsModule
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.adapter.TweetsAdapter
import kotlinx.android.synthetic.main.activity_search_tweets.*
import javax.inject.Inject


class SearchTweetsActivity : AppCompatActivity(),
  SearchTweetsContract.View {

  @Inject
  lateinit var presenter: SearchTweetsContract.Presenter

  private val tweetsAdapter by lazy { TweetsAdapter().apply { onClick = presenter::onTweetClicked } }
  private val searchFAB by lazy { findViewById<FloatingActionButton>(R.id.floating_search) }
  private val searchEdit by lazy { findViewById<EditText>(R.id.edit_search) }
  private val searchError by lazy { findViewById<TextView>(R.id.text_search_error) }
  private val searchProgress by lazy { findViewById<ProgressBar>(R.id.progress_searching_user) }
  private val recyclerTweets by lazy { findViewById<RecyclerView>(R.id.recycler_tweets) }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search_tweets)
    setSupportActionBar(toolbar)

    initInjection()
    initActivity()
    setAdapter()

    presenter.onViewCreated(savedInstanceState, intent.extras)
  }

  private fun initInjection() {
    DaggerSearchTweetsComponent
        .builder()
        .applicationComponent((application as MyApp).getApplicationComponent())
        .searchTweetsModule(SearchTweetsModule(this))
        .build()
        .inject(this)
  }

  private fun initActivity() {
    setFABListener()
    setSearchListener()

    presenter.bindView(this)
  }

  private fun setAdapter() {
    recyclerTweets.setHasFixedSize(true)
    recyclerTweets.layoutManager = LinearLayoutManager(this)
    recyclerTweets.adapter = tweetsAdapter
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    presenter.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  private fun setFABListener() {
    searchFAB.setOnClickListener{ presenter.onSearchFABPressed() }
  }

  private fun setSearchListener() {
    searchEdit.setOnEditorActionListener({ _, actionId, _ ->
      presenter.onSearchUserPressed(searchEdit.text.toString(), actionId)
    })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_search, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_search -> presenter.onSearchMenuPressed()
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun toggleSearchFAB(visible: Boolean) {
    when (visible) {
      true -> searchFAB.visibility = View.VISIBLE
      else -> searchFAB.visibility = View.GONE
    }
  }

  override fun toggleSearchView(visible: Boolean) {
    when (visible) {
      true -> searchEdit.visibility = View.VISIBLE
      else -> searchEdit.visibility = View.GONE
    }
  }

  override fun toggleSearchProgress(visible: Boolean) {
    when (visible) {
      true -> searchProgress.visibility = View.VISIBLE
      else -> searchProgress.visibility = View.GONE
    }
  }

  override fun toggleSearchError(visible: Boolean) {
    searchError.visibility = when (visible) {
      true -> View.VISIBLE
      else -> View.GONE
    }
  }

  override fun toggleTweetsList(visible: Boolean) {
    recyclerTweets.visibility = when (visible) {
      true -> View.VISIBLE
      else -> View.GONE
    }
  }

  override fun updateErrorMessage(user: String) {
    searchError.text = getString(R.string.search_twitter_user_error, user)
  }

  override fun updateTweetsList(tweetsList: List<Tweet>) {
    tweetsAdapter.clear()
    tweetsAdapter.addAll(tweetsList)
    tweetsAdapter.notifyDataSetChanged()
  }

  override fun updateTweet(position: Int) {
    tweetsAdapter.notifyItemChanged(position)
  }

  override fun hideKeyboard() {
    currentFocus?.let {
      val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      inputManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
  }

  override fun networkAvailable() = applicationContext.internetAvailable()

  override fun snackNoInternet() {
    Snackbar.make(
        window.decorView.findViewById(android.R.id.content),
        R.string.noInternetConn,
        Snackbar.LENGTH_SHORT
    ).show()
  }

  override fun snackInvalidUser() {
    Snackbar.make(
        window.decorView.findViewById(android.R.id.content),
        R.string.invalidUser,
        Snackbar.LENGTH_SHORT
    ).show()
  }
}
