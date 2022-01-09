package com.shortcut.di

import com.shortcut.models.mapper.ComicMapper
import com.shortcut.components.dashboard.details.ComicDetailsViewModel
import com.shortcut.components.dashboard.favorite.FavoriteViewModel
import com.shortcut.components.dashboard.home.HomeViewModel
import com.shortcut.components.dashboard.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { ComicMapper() }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { (num: Int) -> ComicDetailsViewModel(get(), get(), num) }
}
