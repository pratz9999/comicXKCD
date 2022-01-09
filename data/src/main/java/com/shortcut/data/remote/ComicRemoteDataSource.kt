package com.shortcut.data.remote

import com.shortcut.data.remote.model.RemoteComic

interface ComicRemoteDataSource {
    suspend fun fetchCurrentComic(): Result<RemoteComic>
    suspend fun fetchComicByNum(comicNum: Int): Result<RemoteComic>
}