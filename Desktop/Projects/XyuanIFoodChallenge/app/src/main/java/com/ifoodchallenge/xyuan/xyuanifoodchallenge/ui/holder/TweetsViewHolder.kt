package com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.holder

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.R
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet

class TweetsViewHolder(
    val view: View
) : BaseViewHolder<Tweet>(view) {

  private val tweetText by lazy { itemView.findViewById(R.id.text_tweet) as TextView }
  private val tweetFeeling by lazy { itemView.findViewById(R.id.image_tweet_feeling) as ImageView }
  private val tweetProgress by lazy { itemView.findViewById(R.id.progress_checking_tweet) as ProgressBar }

  override fun bind(element: Tweet, onClick: (Tweet, Int) -> Unit) {
    tweetText.text = element.text
    setCheck(element.state)
    setFeeling(element.emotion)
    itemView.setOnClickListener {
      if (element.state == 0) {
        onClick.invoke(element, layoutPosition)
      }
    }
  }

  private fun setCheck(state: Int) {
    when (state) {
      Tweet.STATE_CHECKING -> {
        tweetProgress.visibility = View.VISIBLE
        tweetFeeling.visibility = View.GONE
      }
      Tweet.STATE_CHECKED -> {
        tweetProgress.visibility = View.GONE
        tweetFeeling.visibility = View.VISIBLE
      }
      else -> {
        tweetProgress.visibility = View.INVISIBLE
        tweetFeeling.visibility = View.GONE
      }
    }
  }

  private fun setFeeling(feeling: Int) {
    when (feeling) {
      Tweet.FEELING_HAPPY -> tweetFeeling.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_36dp)
      Tweet.FEELING_SAD -> tweetFeeling.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_36dp)
      else -> tweetFeeling.setImageResource(R.drawable.ic_sentiment_neutral_black_36dp)
    }
  }
}