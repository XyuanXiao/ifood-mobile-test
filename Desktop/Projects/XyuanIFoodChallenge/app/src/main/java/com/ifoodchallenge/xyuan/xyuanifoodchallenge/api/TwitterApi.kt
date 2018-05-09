package com.ifoodchallenge.xyuan.xyuanifoodchallenge.api

import com.ifoodchallenge.xyuan.xyuanifoodchallenge.model.Tweet
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitterApi {

  @GET("1.1/statuses/user_timeline.json")
  fun getTweetsFromUser(
      @Query("screen_name") name: String,
      @Query("count") count: String,
      @Header("Request_Identifier") token: String
  ): Observable<List<Tweet>>

  @POST("oauth2/token")
  fun getAuthToken(
      @Query("grant_type") grantType: String
  ): Observable<TwitterToken>
}
