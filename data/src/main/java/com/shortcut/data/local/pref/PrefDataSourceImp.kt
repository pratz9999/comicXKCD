package com.shortcut.data.local.pref

import android.content.SharedPreferences

class PrefDataSourceImp(preferences: SharedPreferences) :
    BasePref(preferences), PrefDataSource {

    companion object {
        const val PREF_NAME = "xkcd_pref"
        private const val KEY_CURRENT_COMIC_NUM = "last_comic_num"
    }

    override fun setLastComicNum(num: Int) {
        setIntegerPreference(KEY_CURRENT_COMIC_NUM, num)
    }

    override fun getLastComicNum() = getIntegerPreference(KEY_CURRENT_COMIC_NUM, 0)


}