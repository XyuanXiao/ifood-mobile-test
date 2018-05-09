package com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.api.TwitterApi
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract.SearchTweetsContract
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.ActivityScope
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.SearchTweetsModel
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.presenter.SearchTweetsPresenter
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.SearchTweetsActivity
import dagger.Module
import dagger.Provides

@Module
class SearchTweetsModule(
    var searchTweetsActivity: SearchTweetsActivity
) {

  @Provides
  @ActivityScope
  fun providesContext(): SearchTweetsActivity = searchTweetsActivity

  @Provides
  @ActivityScope
  fun providesSearchTweetPresenter(
      model: SearchTweetsModel
  ): SearchTweetsContract.Presenter = SearchTweetsPresenter(model)

  @Provides
  @ActivityScope
  fun providesSearchTweetModel(
      api: TwitterApi
  ) = SearchTweetsModel(api)

}