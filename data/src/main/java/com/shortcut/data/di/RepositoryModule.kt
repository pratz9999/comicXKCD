package com.shortcut.data.di

import com.shortcut.data.local.db.ComicDbDataSource
import com.shortcut.data.local.db.ComicDbDataSourceImp
import com.shortcut.data.local.entity.mapper.ComicMapper
import com.shortcut.data.local.pref.PrefDataSource
import com.shortcut.data.local.pref.PrefDataSourceImp
import com.shortcut.data.remote.ComicRemoteDataSource
import com.shortcut.data.remote.ComicRemoteDataSourceImp
import com.shortcut.data.repository.ComicRepository
import com.shortcut.data.repository.ComicRepositoryImp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single { ComicMapper() }

    single<ComicRemoteDataSource> { ComicRemoteDataSourceImp(get()) }
    single<ComicDbDataSource> { ComicDbDataSourceImp(get(), get()) }
    single<PrefDataSource> { PrefDataSourceImp(get(qualifier = named(SHARED_PREF))) }
    single<ComicRepository> {
        ComicRepositoryImp(
            get(),
            get(),
            get(),
            get(),
        )
    }
}
