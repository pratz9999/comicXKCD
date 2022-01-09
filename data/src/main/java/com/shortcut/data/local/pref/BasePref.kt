package com.shortcut.data.local.pref

import android.content.SharedPreferences

open class BasePref(private val sharedPreferences: SharedPreferences) {

    fun setBooleanPreference(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value)
            .apply()
    }

    fun getBooleanPreference(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences
            .getBoolean(key, defaultValue)
    }

    fun setStringPreference(key: String, value: String?) {
        sharedPreferences.edit().putString(key, value)
            .apply()
    }

    fun getStringPreference(key: String, defaultValue: String?): String {
        return sharedPreferences
            .getString(key, defaultValue) ?: ""
    }

    fun getIntegerPreference(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun setIntegerPreference(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun setLongPreference(key: String, value: Long) {
        sharedPreferences.edit().putLong(key, value).apply()
    }

    fun getLongPreference(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun removeKey(key: String) = sharedPreferences.edit().remove(key)

    fun clear() = sharedPreferences.edit().clear().apply()


}