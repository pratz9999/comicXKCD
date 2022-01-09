package com.shortcut.components.dashboard.listener

import com.shortcut.models.ComicView

interface ComicItemListener {
    fun onComicLoaded(num: Int)
    fun onDetailsClicked(comicView: ComicView)
    fun onRefresh(comicView: ComicView)
}