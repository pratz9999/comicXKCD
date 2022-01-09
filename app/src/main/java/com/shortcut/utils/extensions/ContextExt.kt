package com.shortcut.utils.extensions

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.shortcut.utils.Constants
import com.shortcut.xkcd.R

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.share(subject: String, url :String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, url)
        type = Constants.SHARE
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Context.openCustomTabBrowser(url: String) {
    val builder = CustomTabsIntent.Builder().apply {

        val mainColor = ContextCompat.getColor(this@openCustomTabBrowser, R.color.primaryColor)
        val defaultColors = CustomTabColorSchemeParams.Builder()
            .setNavigationBarColor(mainColor)
            .setToolbarColor(mainColor)
            .setSecondaryToolbarColor(mainColor)
            .build()

        setDefaultColorSchemeParams(defaultColors)
        setShowTitle(true)
        setUrlBarHidingEnabled(true)
    }
    try {
        builder.build()
            .launchUrl(this, Uri.parse(url))
    } catch (e: Exception) {
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            this.startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            toast(getString(R.string.error_no_support_application_found))
        }
    }
}
