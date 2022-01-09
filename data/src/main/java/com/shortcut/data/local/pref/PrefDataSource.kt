package com.shortcut.data.local.pref

interface PrefDataSource {
    fun setLastComicNum(num: Int)
    fun getLastComicNum(): Int
}