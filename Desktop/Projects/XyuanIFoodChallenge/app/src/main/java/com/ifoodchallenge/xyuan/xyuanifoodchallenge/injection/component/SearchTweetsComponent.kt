package com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.component

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.ActivityScope
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.injection.module.SearchTweetsModule
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.SearchTweetsActivity
import dagger.Component

@ActivityScope
@Component (
    dependencies = arrayOf(ApplicationComponent::class),
    modules = arrayOf(SearchTweetsModule::class)
)
interface SearchTweetsComponent {
  fun inject(activity: SearchTweetsActivity)
}