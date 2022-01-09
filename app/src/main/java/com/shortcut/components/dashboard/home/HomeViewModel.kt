package com.shortcut.components.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shortcut.data.repository.ComicRepository
import com.shortcut.data.repository.Resource
import com.shortcut.models.ComicView
import com.shortcut.models.mapper.ComicMapper
import com.shortcut.components.dashboard.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ComicRepository,
    private val mapper: ComicMapper
) : BaseViewModel() {

    private var maxComicNum: Int = -1
    private var nextComicNum: Int = -1

    private val _currentComicState: MutableLiveData<Resource<ComicView>> = MutableLiveData()
    val currentComicState: LiveData<Resource<ComicView>> = _currentComicState

    private val _nextComicState: MutableLiveData<Resource<ComicView>> = MutableLiveData()
    val nextComicState: LiveData<Resource<ComicView>> = _nextComicState

    fun getCurrentComic() {
        viewModelScope.launch(handler) {
            repository.getCurrentComic().collect {
                when (it) {
                    is Resource.ERROR -> _currentComicState.postValue(Resource.ERROR(it.exception))
                    is Resource.LOADING -> _currentComicState.postValue(Resource.LOADING(it.isLoading))
                    is Resource.SUCCESS -> {
                        it.data?.let { entity ->
                            _currentComicState.postValue(Resource.SUCCESS(mapper.toModelView(entity)))
                            maxComicNum = entity.num
                            nextComicNum = entity.num - 1
                        }

                    }
                }
            }
        }
    }

    fun getNextComic() {
        if (nextComicNum <= 0) return
        viewModelScope.launch(handler) {
            repository.getComic(nextComicNum).collect {
                when (it) {
                    is Resource.ERROR -> _nextComicState.postValue(Resource.ERROR(it.exception))
                    is Resource.LOADING -> _nextComicState.postValue(Resource.LOADING(it.isLoading))
                    is Resource.SUCCESS -> {
                        it.data?.let { entity ->
                            _nextComicState.postValue(Resource.SUCCESS(mapper.toModelView(entity)))
                            nextComicNum = entity.num - 1
                        }

                    }
                }
            }
        }
    }

    fun retry() {
        if (maxComicNum < 0)
            getCurrentComic()
        else getNextComic()
    }

    fun isLastComic(num: Int): Boolean {
        return num == 1
    }

    fun isFirstComic(num: Int): Boolean {
        return num == maxComicNum
    }
}