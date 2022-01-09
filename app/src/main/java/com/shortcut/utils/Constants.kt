package com.shortcut.utils

import com.shortcut.xkcd.BuildConfig
import java.util.concurrent.TimeUnit

object Constants {

    const val EXPLAIN_BASE_URL = "https://www.explainxkcd.com/wiki/index.php/"
    const val SPLASH_DELAY: Long = 3000 //3 seconds
    const val SHARE = "text/plain"

    fun getMinInterval() = if (BuildConfig.DEBUG) {
        Pair(2L, TimeUnit.MINUTES)
    } else {
        Pair(12L, TimeUnit.HOURS)
    }

    fun getMaxInterval() = if (BuildConfig.DEBUG) {
        Pair(30L, TimeUnit.MINUTES)
    } else {
        Pair(24L, TimeUnit.HOURS)
    }

    fun getShareUrl(comicNum: Int): String {
        return "https://xkcd.com/$comicNum/"
    }
}