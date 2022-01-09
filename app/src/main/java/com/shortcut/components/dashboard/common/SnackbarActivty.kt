package com.shortcut.components.dashboard.common

import com.google.android.material.snackbar.Snackbar

interface SnackbarActivty {
    fun getSnackbar(errorMsg: String): Snackbar
}