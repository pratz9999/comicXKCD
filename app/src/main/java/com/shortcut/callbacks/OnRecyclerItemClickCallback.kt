package com.shortcut.callbacks

import android.view.View

interface OnRecyclerItemClickCallback<T> {
    fun onRecyclerItemClicked(position:Int, view:View, data:T)
}