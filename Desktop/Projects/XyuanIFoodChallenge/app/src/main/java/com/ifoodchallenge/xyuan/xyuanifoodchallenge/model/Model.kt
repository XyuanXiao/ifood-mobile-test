package com.ifoodchallenge.xyuan.xyuanifoodchallenge.model

import android.os.Parcel
import android.os.Parcelable

abstract class Model : Parcelable {
  override fun describeContents() = 0

  open class ParcelableCreator<T>(
      val createFromParcel: (source: Parcel) -> T,
      val newArray: (size: Int) -> Array<T?>
  ) : Parcelable.Creator<T> {

    override fun createFromParcel(source: Parcel?): T? {
      if (source == null) return null
      return this.createFromParcel.invoke(source)
    }

    override fun newArray(size: Int): Array<T?> {
      return newArray.invoke(size)
    }
  }
}