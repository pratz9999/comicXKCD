package com.shortcut.data.local.db

import com.shortcut.data.local.db.dao.ComicDAO
import com.shortcut.data.local.entity.ComicEntity
import com.shortcut.data.local.entity.mapper.ComicMapper

class ComicDbDataSourceImp(private val comicDAO: ComicDAO, val comicMapper: ComicMapper) :
    ComicDbDataSource {

    override suspend fun insert(comic: ComicEntity) =
        comicDAO.insert(comic)

    override suspend fun getComicByNum(comicNum: Int) =
        comicDAO.getComicByNum(comicNum)

    override fun searchComicsByText(text: String) =
        comicDAO.searchComicsByText(text)

    override suspend fun getAllComics() =
        comicDAO.getAllComics()

    override suspend fun setFavorite(comicNum: Int, favorite: Boolean) =
        comicDAO.setFavorite(comicNum, favorite)

    override fun getAllFavoriteComics() = comicDAO.getAllFavoriteComics()

    override fun getComicLiveData(comicNum: Int) =
        comicDAO.getComicLiveData(comicNum)
}