package com.shortcut.data.remote

import com.shortcut.data.remote.service.ComicAPIService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ComicRemoteDataSourceImp(
    private val comicAPIService: ComicAPIService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ComicRemoteDataSource {

    override suspend fun fetchCurrentComic() = withContext(dispatcher) {
        safeApiResult(comicAPIService.fetchCurrentComic())
    }

    override suspend fun fetchComicByNum(comicNum: Int) = withContext(dispatcher) {
        safeApiResult(comicAPIService.fetchComicByNum(comicNum))
    }


    private fun <T> safeApiResult(call: Response<T>): Result<T> {
        if (call.isSuccessful) return Result.Success(call.body())

        when (call.code()) {
            500, 504 -> throw Failure.ServerException(call.message())
            404 -> throw Failure.NotFoundException(call.message())
            //401 -> throw Failure.InvalidAPIKeyException(call.message())
            else -> {
                return Result.Error(Failure.UnknownException(call.message()))
            }
        }
    }
}

