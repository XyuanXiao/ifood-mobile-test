package com.ifoodchallenge.xyuan.xyuanifoodchallenge.model

import android.os.Parcel

class Tweet(
    var emotion: Int,
    var state: Int,
    var text: String
) : Model() {

  constructor(parcel: Parcel)
      : this(
      parcel.readInt(),
      parcel.readInt(),
      parcel.readString()
  )

  override fun writeToParcel(dest: Parcel?, flags: Int) {
    dest?.writeInt(emotion)
    dest?.writeInt(state)
    dest?.writeString(text)
  }

  companion object {
    @JvmField
    val CREATOR = ParcelableCreator(
        createFromParcel = { parcel -> Tweet(parcel) },
        newArray = { size -> arrayOfNulls<Tweet>(size) }
    )

    val FEELING_NEUTRAL = 0
    val FEELING_HAPPY = 1
    val FEELING_SAD = 2
    val STATE_UNCHECKED = 0
    val STATE_CHECKING = 1
    val STATE_CHECKED = 2
  }
}

class Tweets : ArrayList<Tweet>()