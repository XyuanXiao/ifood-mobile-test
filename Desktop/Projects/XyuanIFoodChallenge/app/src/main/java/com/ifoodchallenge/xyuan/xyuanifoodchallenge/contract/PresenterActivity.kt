package com.ifoodchallenge.xyuan.xyuanifoodchallenge.contract

import android.os.Bundle

interface PresenterActivity<V> {
  fun bindView(view: V)
  fun onViewCreated(savedInstanceState: Bundle?, extras: Bundle?)
  fun onSaveInstanceState(bundle: Bundle?)
}