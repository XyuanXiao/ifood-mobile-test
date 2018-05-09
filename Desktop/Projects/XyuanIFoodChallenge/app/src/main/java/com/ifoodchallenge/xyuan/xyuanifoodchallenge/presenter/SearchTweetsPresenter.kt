package com.ifoodchallenge.xyuan.xyuanifoodchallenge.presenter

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.inputmethod.EditorInfo
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract.SearchTweetsContract
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.SearchTweetsModel
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet
import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweets
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SearchTweetsPresenter(
    private val model: SearchTweetsModel
) : SearchTweetsContract.Presenter {

  private val TWEETS_LIST_KEY = "tweets_list_key"

  private lateinit var view: SearchTweetsContract.View
  private var tweetsList = ArrayList<Tweet>()
  private lateinit var selectedTweetText: String
  private var subscriptions = CompositeDisposable()
  private var searchTweetsInProgress = false


  override fun bindView(view: SearchTweetsContract.View) {
    this.view = view
  }

  override fun onViewCreated(savedInstanceState: Bundle?, extras: Bundle?) {
    val bundle = savedInstanceState ?: extras
    bundle?.let {
      tweetsList = it.getParcelableArrayList(TWEETS_LIST_KEY)
      if (!tweetsList.isEmpty()) {
        searchSuccessLayout()
        view.updateTweetsList(tweetsList)
        view.toggleTweetsList(true)
//        subscriptions.add(checkTweetFeeling(selectedTweetText))
      }
    }
  }

//  private fun onTweetPressed(): Subscription {}

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
          subscriptions.add(getTweetsFromApi(user))
          searchTweetsInProgress = true

          tweetsList.clear()
          view.clearTweetsList()

          view.hideKeyboard()
          searchProgressLayout()
        } else {
          Log.d(this.javaClass.name, "No internet connection when trying to get tweets from user")
          view.snackNoInternet()
        }
      } else {
        Log.d(this.javaClass.name, "User search must be more than 2 characters")
        view.snackInvalidUser()
      }
      return true
    }
    return false
  }

  override fun onTweetClicked(tweet: Tweet, position: Int) {
    if(tweet.state == Tweet.STATE_UNCHECKED) {
      //Request to google emotion api
      tweet.emotion = Tweet.FEELING_HAPPY
      tweet.state = Tweet.STATE_CHECKING
      view.updateTweet(position)

      waitAndCheck(tweet, position)
    }
  }

  private fun waitAndCheck(tweet: Tweet, position: Int) {
    val handler = Handler()
    handler.postDelayed({
      tweet.state = Tweet.STATE_CHECKED
      view.updateTweet(position)
    }, 2000)
  }

  private fun getTweetsFromApi(user: String): Disposable {
    return model.getTweetsFromUser(user).subscribe(
        { tweets ->
          searchSuccessLayout()
//          tweetsList = ArrayList(tweets)
        },
        { error ->
          waitAndGo(user)
//          searchErrorLayout(user)
          Log.e(this.javaClass.name, error.message ?: "Error trying to get tweets from user")
        }
    )
  }

//  private fun checkTweetFeeling(text: String): Disposable {
//    return model.checkTweetFeeling(text).subscribe
//  }

  private fun waitAndGo(user: String) {
    val handler = Handler()
    handler.postDelayed({
      searchTweetsInProgress = false
      val mockResp = "[{\"created_at\":\"Wed May 09 11:12:57 +0000 2018\",\"id\":994173373478469633,\"id_str\":\"994173373478469633\",\"text\":\"Party, vacations, beach, love, freinds, family, good will\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":993956921504919554,\"id_str\":\"993956921504919554\",\"name\":\"Xyuan dev account\",\"screen_name\":\"XyuanDev\",\"location\":\"\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":0,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Tue May 08 20:52:51 +0000 2018\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":7,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":null,\"profile_background_image_url_https\":null,\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":null,\"follow_request_sent\":null,\"notifications\":null,\"translator_type\":\"none\"},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},{\"created_at\":\"Wed May 09 11:11:45 +0000 2018\",\"id\":994173071023116289,\"id_str\":\"994173071023116289\",\"text\":\"Sadness, bad, blue, tired, unhappy, dead, rot\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":993956921504919554,\"id_str\":\"993956921504919554\",\"name\":\"Xyuan dev account\",\"screen_name\":\"XyuanDev\",\"location\":\"\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":0,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Tue May 08 20:52:51 +0000 2018\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":7,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":null,\"profile_background_image_url_https\":null,\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":null,\"follow_request_sent\":null,\"notifications\":null,\"translator_type\":\"none\"},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},{\"created_at\":\"Wed May 09 11:09:46 +0000 2018\",\"id\":994172569547919360,\"id_str\":\"994172569547919360\",\"text\":\"Neutral things, such a common community with normal persons and political neutrality. Neutral words like car, air, internet, paper...\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":993956921504919554,\"id_str\":\"993956921504919554\",\"name\":\"Xyuan dev account\",\"screen_name\":\"XyuanDev\",\"location\":\"\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":0,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Tue May 08 20:52:51 +0000 2018\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":7,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":null,\"profile_background_image_url_https\":null,\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":null,\"follow_request_sent\":null,\"notifications\":null,\"translator_type\":\"none\"},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},{\"created_at\":\"Wed May 09 11:07:32 +0000 2018\",\"id\":994172008652005376,\"id_str\":\"994172008652005376\",\"text\":\"Ok, now everything is fine again. It passed and I will be the happiest person alive. Yes!\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":993956921504919554,\"id_str\":\"993956921504919554\",\"name\":\"Xyuan dev account\",\"screen_name\":\"XyuanDev\",\"location\":\"\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":0,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Tue May 08 20:52:51 +0000 2018\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":7,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":null,\"profile_background_image_url_https\":null,\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":null,\"follow_request_sent\":null,\"notifications\":null,\"translator_type\":\"none\"},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},{\"created_at\":\"Wed May 09 11:06:42 +0000 2018\",\"id\":994171801205866496,\"id_str\":\"994171801205866496\",\"text\":\"Oh, I don't believe it! It's so sad! Right when things were going well, now I ruined everything forever. I'm so neg\\u2026 https:\\/\\/t.co\\/NiRBWQl3YX\",\"truncated\":true,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/NiRBWQl3YX\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/994171801205866496\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/9\\u2026\",\"indices\":[117,140]}]},\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":993956921504919554,\"id_str\":\"993956921504919554\",\"name\":\"Xyuan dev account\",\"screen_name\":\"XyuanDev\",\"location\":\"\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":0,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Tue May 08 20:52:51 +0000 2018\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":7,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":null,\"profile_background_image_url_https\":null,\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":null,\"follow_request_sent\":null,\"notifications\":null,\"translator_type\":\"none\"},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},{\"created_at\":\"Wed May 09 11:04:45 +0000 2018\",\"id\":994171309209931777,\"id_str\":\"994171309209931777\",\"text\":\"Now it's getting better! I can feel so much positiveness and good feelings in the air. It's a nice day, folks!\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":993956921504919554,\"id_str\":\"993956921504919554\",\"name\":\"Xyuan dev account\",\"screen_name\":\"XyuanDev\",\"location\":\"\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":0,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Tue May 08 20:52:51 +0000 2018\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":7,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":null,\"profile_background_image_url_https\":null,\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":null,\"follow_request_sent\":null,\"notifications\":null,\"translator_type\":\"none\"},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"},{\"created_at\":\"Wed May 09 11:03:49 +0000 2018\",\"id\":994171073427050497,\"id_str\":\"994171073427050497\",\"text\":\"Initial tweet. Everything is so neutral until now. Its normal to be common.\",\"truncated\":false,\"entities\":{\"hashtags\":[],\"symbols\":[],\"user_mentions\":[],\"urls\":[]},\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\\" rel=\\\"nofollow\\\"\\u003eTwitter Web Client\\u003c\\/a\\u003e\",\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":993956921504919554,\"id_str\":\"993956921504919554\",\"name\":\"Xyuan dev account\",\"screen_name\":\"XyuanDev\",\"location\":\"\",\"description\":\"\",\"url\":null,\"entities\":{\"description\":{\"urls\":[]}},\"protected\":false,\"followers_count\":0,\"friends_count\":0,\"listed_count\":0,\"created_at\":\"Tue May 08 20:52:51 +0000 2018\",\"favourites_count\":0,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"verified\":false,\"statuses_count\":7,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"is_translation_enabled\":false,\"profile_background_color\":\"F5F8FA\",\"profile_background_image_url\":null,\"profile_background_image_url_https\":null,\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_image_url_https\":\"https:\\/\\/abs.twimg.com\\/sticky\\/default_profile_images\\/default_profile_normal.png\",\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"has_extended_profile\":false,\"default_profile\":true,\"default_profile_image\":true,\"following\":null,\"follow_request_sent\":null,\"notifications\":null,\"translator_type\":\"none\"},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"favorited\":false,\"retweeted\":false,\"lang\":\"en\"}]"
      try {
        tweetsList = Gson().fromJson(mockResp, Tweets::class.java)
        searchSuccessLayout()
        view.updateTweetsList(tweetsList)
        view.toggleTweetsList(true)
      } catch (e: JsonSyntaxException) {
        searchErrorLayout(user)
        Log.e(this.javaClass.name, e.message)
      }
    }, 1000)
  }

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
