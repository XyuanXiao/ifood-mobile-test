package com.ifoodchallenge.xyuan.xyuanifoodchallenge.extention

import android.content.Context
import android.net.ConnectivityManager

fun Context.internetAvailable(): Boolean {
  val conMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  val activeNetwork = conMgr.activeNetworkInfo
  return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}