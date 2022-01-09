package com.shortcut.data.local.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shortcut.data.local.entity.ComicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(comic: ComicEntity): Long

    @Query("SELECT * FROM comics_table WHERE num =:comicNum")
    suspend fun getComicByNum(comicNum: Int): ComicEntity?

    @Query("SELECT * FROM comics_table WHERE num =:comicNum")
    fun getComicLiveData(comicNum: Int): LiveData<ComicEntity?>

    @Query("""SELECT * FROM comics_table WHERE title LIKE '%' || :text || '%'
        OR transcript LIKE '%' || :text || '%'
        OR alt LIKE '%' || :text || '%' ORDER BY num ASC""")
    fun searchComicsByText(text: String): Flow<List<ComicEntity>>

    @Query("SELECT * FROM comics_table")
    suspend fun getAllComics(): MutableList<ComicEntity>?

    @Query("UPDATE comics_table SET isFavorite = :favorite WHERE num= :comicNum")
    suspend fun setFavorite(comicNum: Int, favorite:Boolean)


    @Query("SELECT * FROM comics_table WHERE isFavorite = 1 ORDER BY num ASC")
    fun getAllFavoriteComics(): LiveData<MutableList<ComicEntity>>

}