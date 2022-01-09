package com.shortcut.data.di

import com.shortcut.data.BuildConfig
import com.shortcut.data.remote.service.ComicAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), BuildConfig.BASE_URL) }
    single { provideComicApiService(get()) }
}

private fun provideOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient
        .Builder()
        .connectTimeout(40, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .readTimeout(40, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(loggingInterceptor)
    }

    return builder.build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

private fun provideComicApiService(retrofit: Retrofit): ComicAPIService =
    retrofit.create(ComicAPIService::class.java)