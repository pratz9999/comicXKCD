package com.shortcut.components.dashboard.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.shortcut.data.local.entity.ComicEntity
import com.shortcut.data.repository.ComicRepository
import com.shortcut.models.ComicView
import com.shortcut.models.mapper.ComicMapper
import com.shortcut.components.dashboard.base.BaseViewModel
import kotlinx.coroutines.launch

class ComicDetailsViewModel(
    private val repository: ComicRepository,
    private val mapper: ComicMapper,
    private val comicNum: Int,
) : BaseViewModel() {

    // the LiveData from Room won't be exposed to the view...
    private var dbComic: LiveData<ComicEntity?> = repository.getComicLiveData(comicNum)

    // ...because this is what we'll want to expose
    val comicMLiveData = MediatorLiveData<ComicView>()

    init {
        comicMLiveData.removeSource(dbComic)
        comicMLiveData.addSource(dbComic) {
            it?.let { comic -> comicMLiveData.value = mapper.toModelView(comic) }
        }
    }

    fun setFavorite(favorite: Boolean) {
        viewModelScope.launch {
            repository.setFavorite(comicNum, favorite)
        }
    }
}