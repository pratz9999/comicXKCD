package com.shortcut.data.remote.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RemoteComic(
    @SerializedName("alt")
    val alt: String = "", // Don't we all.
    @SerializedName("day")
    val day: String = "", // 1
    @SerializedName("img")
    val img: String = "",
    @SerializedName("link")
    val link: String = "",
    @SerializedName("month")
    val month: String = "", // 1
    @SerializedName("news")
    val news: String = "",
    @SerializedName("num")
    val num: Int = 0, // 1
    @SerializedName("safe_title")
    val safeTitle: String = "", // Barrel - Part 1
    @SerializedName("title")
    val title: String = "", // Barrel - Part 1
    @SerializedName("transcript")
    val transcript: String = "",
    @SerializedName("year")
    val year: String = "" // 2006
)