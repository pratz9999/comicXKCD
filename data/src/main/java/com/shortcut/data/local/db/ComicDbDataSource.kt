package com.shortcut.data.local.db

import androidx.lifecycle.LiveData
import com.shortcut.data.local.entity.ComicEntity
import kotlinx.coroutines.flow.Flow

interface ComicDbDataSource {
    suspend fun insert(comic: ComicEntity): Long
    suspend fun getComicByNum(comicNum: Int): ComicEntity?
    fun searchComicsByText(text: String): Flow<List<ComicEntity>>
    suspend fun getAllComics(): MutableList<ComicEntity>?
    suspend fun setFavorite(comicNum: Int, favorite:Boolean)
    fun getAllFavoriteComics(): LiveData<MutableList<ComicEntity>>
    fun getComicLiveData(comicNum: Int): LiveData<ComicEntity?>
}