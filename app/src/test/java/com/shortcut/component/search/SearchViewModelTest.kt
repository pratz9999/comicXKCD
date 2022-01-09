package com.shortcut.component.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.shortcut.components.dashboard.search.SearchViewModel
import com.shortcut.components.dashboard.search.SearchViewState
import com.shortcut.data.local.entity.ComicEntity
import com.shortcut.data.remote.Failure
import com.shortcut.data.repository.ComicRepository
import com.shortcut.data.repository.Resource
import com.shortcut.models.ComicView
import com.shortcut.models.mapper.ComicMapper
import com.shortcut.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    companion object {
        const val EMPTY_TEXT = ""
        const val CURRENT_COMIC_NUM = 2000
        const val GREATER_CURRENT_COMIC_NUM = "2100"
        const val IN_RANGE_COMIC_NUM = "1600"
        const val NEG_COMIC_NUM = "-100"
        const val ZERO_COMIC_NUM = "0"
        const val EXIST_TEXT = "exist"
        const val NOT_EXIST_TEXT = "not exists"
        const val RESULT_ERROR_MSG = "not found"

        val COMICS_ENTITY_RESULT_ITEM = ComicEntity(-1, day = 1, month = 1, year = 1)
        val COMICS_VIEW_RESULT_ITEM = ComicView(-1, day = 1, month = 1, year = 1)
        val COMICS_ENTITY_RESULT_LIST = listOf(ComicEntity(-1, day = 1, month = 1, year = 1))
        val COMICS_RESULT_LIST = mutableListOf(ComicView(-1, day = 1, month = 1, year = 1))
        //endregion constants
    }

    //region helper fields
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var repository: ComicRepository

    @Mock
    private lateinit var searchStateObserver: Observer<SearchViewState>

    @Before
    fun setup() {
        hasCurrentComic()
        searchViewModel = SearchViewModel(repository, ComicMapper())
    }

    //case 1: search with empty query, Post Invalid Data State
    @Test
    fun `searchComic EmptyQueryText InvalidDataStateObserved`() {
        // Arrange
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(EMPTY_TEXT)

        // Assert
        verify(repository, never()).searchComicsByText(EMPTY_TEXT)
        verify(repository, never()).getComic(anyInt())
        verify(searchStateObserver).onChanged(SearchViewState.InvalidSearchData)

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 2: search with Number less than 0, cache called and Post SuccessResult
    @Test
    fun `searchComic NegativeTextQuery SuccessStateObserved`() {
        // Arrange
        successSearchByTextWithExistsResult(NEG_COMIC_NUM)
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(NEG_COMIC_NUM)

        // Assert
        verify(repository, never()).getComic(NEG_COMIC_NUM.toInt())
        verify(repository, times(1)).searchComicsByText(NEG_COMIC_NUM)
        verify(searchStateObserver).onChanged(SearchViewState.Loading(true))
        verify(searchStateObserver).onChanged(SearchViewState.SuccessByText(COMICS_RESULT_LIST))

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 3: search with Number equal 0, cache called and Post SuccessResult
    @Test
    fun `searchComic ZeroTextQuery SuccessStateObserved`() {
        // Arrange
        successSearchByTextWithExistsResult(ZERO_COMIC_NUM)
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(ZERO_COMIC_NUM)

        // Assert
        verify(repository, never()).getComic(ZERO_COMIC_NUM.toInt())
        verify(repository, times(1)).searchComicsByText(ZERO_COMIC_NUM)
        verify(searchStateObserver).onChanged(SearchViewState.Loading(true))
        verify(searchStateObserver).onChanged(SearchViewState.SuccessByText(COMICS_RESULT_LIST))

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 4: search with Number greater Than current comic number, cache called and Post SuccessResult
    @Test
    fun `searchComic NumGreaterThanLastComicQuery SearchByText And SuccessStateObserved`() {
        // Arrange
        successSearchByTextWithExistsResult(GREATER_CURRENT_COMIC_NUM)
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(GREATER_CURRENT_COMIC_NUM)

        // Assert
        verify(repository, never()).getComic(GREATER_CURRENT_COMIC_NUM.toInt())
        verify(repository, times(1)).searchComicsByText(GREATER_CURRENT_COMIC_NUM)
        verify(searchStateObserver).onChanged(SearchViewState.Loading(true))
        verify(searchStateObserver).onChanged(SearchViewState.SuccessByText(COMICS_RESULT_LIST))

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 5: search with Number in range but The current comic never called, Remote called and Success result
    @Test
    fun `searchComic NumInRangeComicQuery SearchByNum should called and SuccessStateObserved`() {
        // Arrange
        unknownCurrentComic()
        successSearchByNUM(IN_RANGE_COMIC_NUM.toInt())
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(IN_RANGE_COMIC_NUM)

        // Assert
        verify(repository, times(1)).getComic(IN_RANGE_COMIC_NUM.toInt())
        verify(repository, never()).searchComicsByText(IN_RANGE_COMIC_NUM)
        verify(searchStateObserver).onChanged(SearchViewState.Loading(true))
        verify(searchStateObserver).onChanged(SearchViewState.SuccessByNum(COMICS_VIEW_RESULT_ITEM))

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 6: search with text exists, Post SuccessByText with list
    @Test
    fun `searchComic ExistsTextQuery SuccessStateObserved`() {
        // Arrange
        successSearchByTextWithExistsResult(EXIST_TEXT)
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(EXIST_TEXT)

        // Assert
        verify(repository, times(1)).searchComicsByText(EXIST_TEXT)
        verify(searchStateObserver).onChanged(SearchViewState.Loading(true))
        verify(searchStateObserver).onChanged(SearchViewState.SuccessByText(COMICS_RESULT_LIST))

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 7: search with text not exists, Post SuccessByText with empty list
    @Test
    fun `searchComic NotExistsTextQuery SuccessStateObserved`() {
        // Arrange
        successSearchByTextNoResult(NOT_EXIST_TEXT)
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(NOT_EXIST_TEXT)

        // Assert
        verify(repository, times(1)).searchComicsByText(NOT_EXIST_TEXT)
        verify(searchStateObserver).onChanged(SearchViewState.Loading(true))
        verify(searchStateObserver).onChanged(SearchViewState.NoSearchResult)

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 8: search with Number greater Than current comic number
    // where The current comic never called, SearchViewStateError Result
    @Test
    fun `searchComic NumGreaterThanLastComicQuery_And_UnknownCurrentNum SearchByNum and ErrorStateObserved`() {
        // Arrange
        unknownCurrentComic()
        notFound(GREATER_CURRENT_COMIC_NUM.toInt())
        searchViewModel.searchState.observeForever(searchStateObserver)

        // Act
        searchViewModel.searchForComics(GREATER_CURRENT_COMIC_NUM)

        // Assert
        verify(repository, times(1)).getComic(GREATER_CURRENT_COMIC_NUM.toInt())
        verify(repository, never()).searchComicsByText(anyString())
        verify(searchStateObserver).onChanged(SearchViewState.Loading(true))
        verify(searchStateObserver).onChanged(SearchViewState.Error(RESULT_ERROR_MSG))

        searchViewModel.searchState.removeObserver(searchStateObserver)
    }

    //case 9: test set favorite
    @Test
    fun `setFavorite AddFav TrueParamPassedToCache`() {
        // Arrange
        //Act
        searchViewModel.setFavorite(true, IN_RANGE_COMIC_NUM.toInt())
        //Assert
        testCoroutineRule.runBlockingTest {
            verify(repository, times(1)).setFavorite(IN_RANGE_COMIC_NUM.toInt(), true)
        }
    }

    //region helper methods
    private fun hasCurrentComic() {
        `when`(repository.getLastComicNum()).thenReturn(CURRENT_COMIC_NUM)
    }

    private fun unknownCurrentComic() {
        `when`(repository.getLastComicNum()).thenReturn(0)
    }

    private fun successSearchByTextWithExistsResult(text: String) {
        `when`(repository.searchComicsByText(text)).then {
            flow {
                emit(COMICS_ENTITY_RESULT_LIST)
            }
        }
    }

    private fun successSearchByTextNoResult(text: String) {
        `when`(repository.searchComicsByText(text)).then {
            flow {
                emit(emptyList<ComicEntity>())
            }
        }
    }

    private fun successSearchByNUM(num: Int) {
        `when`(repository.getComic(num)).then {
            flow {
                emit(Resource.LOADING(true))
                emit(Resource.SUCCESS(COMICS_ENTITY_RESULT_ITEM))
            }
        }
    }

    private fun notFound(num: Int) {
        `when`(repository.getComic(num)).then {
            flow<Resource<ComicEntity>> {
                emit(Resource.LOADING(true))
                emit(Resource.ERROR(Failure.NotFoundException(RESULT_ERROR_MSG)))
            }
        }
    }
}