package com.shortcut.data.di

import android.app.Application
import androidx.room.Room
import com.shortcut.data.local.db.AppDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): AppDataBase {
        return Room.databaseBuilder(application, AppDataBase::class.java, AppDataBase.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideComicDao(database: AppDataBase) = database.getComicDao()

    single { provideDatabase(androidApplication()) }

    single { provideComicDao(get()) }
}