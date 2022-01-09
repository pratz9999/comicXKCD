package com.shortcut.components.dashboard.search

import com.shortcut.models.ComicView

sealed class SearchViewState {
    data class SuccessByNum(val comicView: ComicView) : SearchViewState()
    data class SuccessByText(val comicViewList: MutableList<ComicView>?) : SearchViewState()
    data class Error(val msg: String) : SearchViewState()
    object InvalidSearchData : SearchViewState()
    object NoSearchResult : SearchViewState()
    data class Loading(val isLoading: Boolean) : SearchViewState()
}