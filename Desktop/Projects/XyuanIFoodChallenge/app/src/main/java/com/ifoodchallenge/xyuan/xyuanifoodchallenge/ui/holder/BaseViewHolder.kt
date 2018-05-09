package com.ifoodchallenge.xyuan.xyuanifoodchallenge.ui.holder

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(
    view: View
) : RecyclerView.ViewHolder(view) {
  abstract fun bind(element: T, onClick: (T, Int) -> Unit)
}