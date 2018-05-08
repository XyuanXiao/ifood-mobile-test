package com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.MyApp
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.R
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract.SearchTweetsContract
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.component.DaggerSearchTweetsComponent
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module.SearchTweetsModule
import kotlinx.android.synthetic.main.activity_search_tweets.*
import javax.inject.Inject


class SearchTweetsActivity : AppCompatActivity(),
  SearchTweetsContract.View {

  @Inject
  lateinit var presenter: SearchTweetsContract.Presenter

  private val searchFAB by lazy { findViewById<FloatingActionButton>(R.id.floating_search) }
  private val searchEdit by lazy { findViewById<EditText>(R.id.edit_search) }
  private val searchError by lazy { findViewById<TextView>(R.id.text_search_error) }
  private val searchProgress by lazy { findViewById<ProgressBar>(R.id.progress_searching_user) }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search_tweets)
    setSupportActionBar(toolbar)

    initInjection()
    initActivity()

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

  private fun setFABListener() {
    searchFAB.setOnClickListener{ presenter.onSearchFABPressed() }
  }

  private fun setSearchListener() {
    searchEdit.setOnEditorActionListener({ _, actionId, _ -> presenter.onSearchUserPressed(actionId) })
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
    when (visible) {
      true -> searchError.visibility = View.VISIBLE
      else -> searchError.visibility = View.GONE
    }
  }
}
