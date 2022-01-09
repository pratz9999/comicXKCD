package com.shortcut.models

data class ComicView(
    val num: Int,
    val title: String = "",
    val transcript: String = "",
    val alt: String = "",
    val imgUrl: String? = null,
    val year: Int = 0,
    val month: Int = 0,
    val day: Int = 0,
    var isFavorite: Boolean = false,
    var isLoading :Boolean = false,
)
