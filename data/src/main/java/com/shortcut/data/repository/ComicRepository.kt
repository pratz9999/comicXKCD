package com.shortcut.data.repository

import androidx.lifecycle.LiveData
import com.shortcut.data.local.entity.ComicEntity
import kotlinx.coroutines.flow.Flow

/***
 * @author mahmoud.shawky
 *
 * Repository is an interface the should be implemented to access the application data layer
 */
interface ComicRepository {
    fun getCurrentComic(): Flow<Resource<ComicEntity>>
    fun getComic(num: Int): Flow<Resource<ComicEntity>>
    suspend fun setFavorite(comicNum: Int, favorite:Boolean)
    fun getAllFavoriteComics(): LiveData<MutableList<ComicEntity>>
    fun getComicLiveData(comicNum: Int): LiveData<ComicEntity?>
    fun searchComicsByText(text: String): Flow<List<ComicEntity>>
    fun getLastComicNum() : Int
}


