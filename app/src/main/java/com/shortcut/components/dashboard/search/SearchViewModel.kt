package com.shortcut.components.dashboard.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shortcut.data.repository.ComicRepository
import com.shortcut.data.repository.Resource
import com.shortcut.models.mapper.ComicMapper
import com.shortcut.components.dashboard.base.BaseViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ComicRepository,
    private val mapper: ComicMapper
) : BaseViewModel() {

    private var maxComicNum: Int = Int.MAX_VALUE
    private val _searchState: MutableLiveData<SearchViewState> = MutableLiveData()
    val searchState: LiveData<SearchViewState> = _searchState

    fun searchForComics(query: String?) {
        if (query.isNullOrBlank()) {
            _searchState.value = SearchViewState.InvalidSearchData
            return
        }

        maxComicNum = repository.getLastComicNum()
         if (query.toIntOrNull() in 1..maxComicNum || maxComicNum == 0) {
            getComicByNum(query.toInt())
        } else {
            getComicsByText(query)
        }
    }

    fun setFavorite(favorite: Boolean, comicNum: Int) {
        viewModelScope.launch {
            repository.setFavorite(comicNum, favorite)
        }
    }

    private fun getComicsByText(query: String) {
        viewModelScope.launch {
            _searchState.postValue(SearchViewState.Loading(true))

            repository.searchComicsByText(query).cancellable().collect {
                val list = it.map { entity -> mapper.toModelView(entity) }.toMutableList()
                if (list.isNullOrEmpty())
                    _searchState.postValue(SearchViewState.NoSearchResult)
                else
                    _searchState.postValue(SearchViewState.SuccessByText(list))

                cancel()
            }
        }
    }

    private fun getComicByNum(comicNum: Int) {
        viewModelScope.launch(handler) {
            repository.getComic(comicNum).collect {
                when (it) {
                    is Resource.ERROR -> {
                        it.exception?.msg?.let { msg ->
                            _searchState.postValue(SearchViewState.Error(msg))
                        }
                    }
                    is Resource.LOADING -> {
                        _searchState.postValue(SearchViewState.Loading(it.isLoading))
                    }
                    is Resource.SUCCESS -> {
                        it.data?.let { entity ->
                            _searchState.postValue(
                                SearchViewState.SuccessByNum(
                                    mapper.toModelView(
                                        entity
                                    )
                                )
                            )
                        }

                    }
                }
            }
        }
    }
}