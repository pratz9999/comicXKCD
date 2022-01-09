package com.shortcut.utils.extensions

import android.view.View
import androidx.fragment.app.Fragment
import com.shortcut.callbacks.OnSingleClickListener


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}