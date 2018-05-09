package com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.R
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.holder.BaseViewHolder
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.holder.TweetsViewHolder

class TweetsAdapter: BaseListAdapter<Tweet>() {

  companion object {
    const val LAYOUT_ID = R.layout.view_tweet
  }

  var onClick: (Tweet, Int) -> Unit = { _, position -> notifyItemChanged(position) }

  override fun onBindViewHolder(holder: BaseViewHolder<Tweet>, position: Int) {
    if (holder is TweetsViewHolder) {
      holder.bind(get(position), onClick)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Tweet> {
    val inflatedView = LayoutInflater
        .from(parent.context)
        .inflate(LAYOUT_ID, parent, false)

    return TweetsViewHolder(inflatedView)
  }
}
