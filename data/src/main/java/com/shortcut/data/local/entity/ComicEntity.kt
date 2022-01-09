package com.shortcut.data.local.entity


import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics_table")
@Keep
data class ComicEntity(
    @PrimaryKey
    @ColumnInfo(name = "num")
    val num: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "transcript")
    val transcript: String = "",

    @ColumnInfo(name = "alt")
    val alt: String = "",

    @ColumnInfo(name = "img")
    val imgUrl: String? = null,

    @ColumnInfo(name = "year")
    val year: Int,

    @ColumnInfo(name = "month")
    val month: Int,

    @ColumnInfo(name = "day")
    val day: Int,

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean = false,
)