package com.ifoodchallenge.xyuan.xyuanifoodchallenge.presenter

import android.os.Bundle

interface BasePresenter<V> {
  fun bindView(view: V)
  fun onViewCreated(savedInstanceState: Bundle?, extras: Bundle?)
  fun onSaveInstanceState(bundle: Bundle?)
}