package com.shortcut.data.remote.service

import com.shortcut.data.remote.model.RemoteComic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ComicAPIService {

    @GET("info.0.json")
    suspend fun fetchCurrentComic(): Response<RemoteComic>

    @GET("{comicNum}/info.0.json")
    suspend fun fetchComicByNum(
        @Path("comicNum") comicNum: Int
    ): Response<RemoteComic>

}