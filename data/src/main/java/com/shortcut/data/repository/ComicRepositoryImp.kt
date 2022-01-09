package com.shortcut.data.repository

import com.shortcut.data.local.db.ComicDbDataSource
import com.shortcut.data.local.entity.ComicEntity
import com.shortcut.data.local.entity.mapper.ComicMapper
import com.shortcut.data.local.pref.PrefDataSource
import com.shortcut.data.remote.ComicRemoteDataSource
import com.shortcut.data.remote.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ComicRepositoryImp(
    private val remoteDataSource: ComicRemoteDataSource,
    private val cacheDataSource: ComicDbDataSource,
    private val prefDataSource: PrefDataSource,
    val mapper: ComicMapper,
) : ComicRepository {

    /***
     * We will depend on this API to get the last comic
     * Cache it, save it's num
     * So we will able to load old comics and notify the user when new added
     *
     */
    override fun getCurrentComic() =
        flow<Resource<ComicEntity>> {
            emit(Resource.LOADING(true))
            when (val result = remoteDataSource.fetchCurrentComic()) {
                is Result.Success -> {
                    result.data?.let {
                        val comicEntity = mapper.toDbEntity(it)
                        prefDataSource.setLastComicNum(comicEntity.num)
                        cacheDataSource.insert(comicEntity)
                        emit(Resource.SUCCESS(comicEntity))
                    }
                }
                is Result.Error -> {
                    emit(Resource.ERROR(result.exception))
                }
            }

        }.flowOn(Dispatchers.IO)

    override fun getComic(num: Int): Flow<Resource<ComicEntity>> =
        flow<Resource<ComicEntity>> {
            emit(Resource.LOADING(true))
            val cachedComic = cacheDataSource.getComicByNum(num)
            if (cachedComic != null) {
                emit(Resource.SUCCESS(cachedComic))
            } else {
                when (val result = remoteDataSource.fetchComicByNum(num)) {
                    is Result.Success -> {
                        result.data?.let {
                            val comic = mapper.toDbEntity(it)
                            cacheDataSource.insert(comic)
                            emit(Resource.SUCCESS(comic))
                        }
                    }
                    is Result.Error -> {
                        emit(Resource.ERROR(result.exception))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun setFavorite(comicNum: Int, favorite: Boolean) {
        cacheDataSource.setFavorite(comicNum, favorite)
    }

    override fun getAllFavoriteComics() = cacheDataSource.getAllFavoriteComics()

    override fun getComicLiveData(comicNum: Int) = cacheDataSource.getComicLiveData(comicNum)

    //Because I can't find a working search API, we will depend on locale search
    override fun searchComicsByText(text: String) = cacheDataSource.searchComicsByText(text)

    override fun getLastComicNum() = prefDataSource.getLastComicNum()

}