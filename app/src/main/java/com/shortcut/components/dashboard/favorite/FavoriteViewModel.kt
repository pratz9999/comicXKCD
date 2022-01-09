package com.shortcut.components.dashboard.favorite

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.shortcut.data.repository.ComicRepository
import com.shortcut.models.ComicView
import com.shortcut.models.mapper.ComicMapper
import com.shortcut.components.dashboard.base.BaseViewModel
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: ComicRepository,
    private val mapper: ComicMapper,
) : BaseViewModel() {

    // the LiveData from Room won't be exposed to the view...
    private var dbFavorites = repository.getAllFavoriteComics()

    // ...because this is what we'll want to expose
    val favoriteListMLiveData = MediatorLiveData<MutableList<ComicView>>()

/*    val favoriteList: LiveData<MutableList<ComicView>> =
        Transformations.switchMap(repository.getAllFavoriteComics()) {
        it.map { mapper.toModelView(it) }
    }*/

    init {
        addCacheListener()
    }

    private fun addCacheListener() {
        favoriteListMLiveData.removeSource(dbFavorites)
        favoriteListMLiveData.addSource(dbFavorites) {
            viewModelScope.launch {
                val list = it.map { entity -> mapper.toModelView(entity) }.toMutableList()
                favoriteListMLiveData.postValue(list)
            }
        }

    }

    fun setFavorite(favorite: Boolean, comicNum: Int) {
        viewModelScope.launch {
            repository.setFavorite(comicNum, favorite)
        }
    }
}