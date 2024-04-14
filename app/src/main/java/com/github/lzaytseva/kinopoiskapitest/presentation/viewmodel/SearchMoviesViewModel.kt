package com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.ErrorEntity
import com.github.lzaytseva.kinopoiskapitest.domain.api.FiltersInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.api.SearchMovieInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.model.Movie
import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MoviesSearchScreenState
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MoviesSearchSideEffect
import com.github.lzaytseva.kinopoiskapitest.util.BaseViewModel
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import com.github.lzaytseva.kinopoiskapitest.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchMoviesViewModel @Inject constructor(
    private val searchMovieInteractor: SearchMovieInteractor,
    private val filtersInteractor: FiltersInteractor
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<MoviesSearchScreenState> =
        MutableStateFlow(MoviesSearchScreenState.Initial)
    val uiState: StateFlow<MoviesSearchScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<MoviesSearchSideEffect> =
        MutableSharedFlow()
    val sideEffects: SharedFlow<MoviesSearchSideEffect> = _sideEffects.asSharedFlow()

    private var searchMode: SearchMode = SearchMode.AllMovies

    private var pagesTotal: Int = 0
    private var currentPage: Int = 0
    private var isNextPageLoading = false
    private val movies = mutableListOf<Movie>()

    private var latestSearchText: String? = null
    private val moviesSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY_IN_MILLIS, viewModelScope, true) { changedText ->
            searchRequest(changedText)
        }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            filtersInteractor.preloadValues()
        }
    }

    fun loadAllMovies() {
        movies.clear()
        searchMode = SearchMode.AllMovies

        viewModelScope.launch {
            _uiState.value = MoviesSearchScreenState.Loading

            searchMovieInteractor.getAllMovies().collect {
                processResult(resource = it)
            }
        }
    }

    fun loadNextPage() {
        if (isNextPageLoading) return
        if (currentPage == pagesTotal) return

        viewModelScope.launch {
            isNextPageLoading = true
            _uiState.value = MoviesSearchScreenState.LoadingNextPage

            when (searchMode) {
                SearchMode.AllMovies -> loadAllMoviesNextPage()
                SearchMode.SearchByParams -> loadSearchByParamsNextPage()
                SearchMode.SearchByTitle -> loadSearchByTitleNextPage()
            }
        }
    }

    private suspend fun loadAllMoviesNextPage() {
        searchMovieInteractor.loadAllMoviesNextPage().collect {
            processResult(resource = it)
        }
    }

    private suspend fun loadSearchByTitleNextPage() {
        searchMovieInteractor.loadSearchByTitleNextPage().collect {
            processResult(resource = it)
        }
    }

    private suspend fun loadSearchByParamsNextPage() {
        searchMovieInteractor.loadSearchByParamsNextPage().collect {
            processResult(resource = it)
        }
    }

    fun searchMoviesByTitle(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            moviesSearchDebounce(changedText)
        }
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isBlank()) return
        movies.clear()
        searchMode = SearchMode.SearchByTitle

        viewModelScope.launch {
            _uiState.value = MoviesSearchScreenState.Loading

            searchMovieInteractor.searchMoviesByTitle(searchQuery = newSearchText).collect {
                processResult(resource = it)
            }
        }
    }

    private suspend fun processResult(resource: Resource<MoviesSearchResult>) {
        when (resource) {
            is Resource.Error -> {
                handleError(resource.error)
            }

            is Resource.Success -> {
                handleSuccess(resource)
            }
        }
    }

    private suspend fun handleError(error: ErrorEntity) {
        when (error) {
            ErrorEntity.NoInternet -> {
                if (isNextPageLoading) {
                    _sideEffects.emit(MoviesSearchSideEffect.NoInternet)
                } else {
                    _uiState.value = MoviesSearchScreenState.Error.NoInternet
                }
            }

            ErrorEntity.ServerError -> {
                if (isNextPageLoading) {
                    _sideEffects.emit(MoviesSearchSideEffect.ServerError)
                } else {
                    _uiState.value = MoviesSearchScreenState.Error.ServerError
                }
            }

            is ErrorEntity.UndefinedError -> {
                if (isNextPageLoading) {
                    _sideEffects.emit(MoviesSearchSideEffect.UndefinedError(error.message))
                } else {
                    _uiState.value = MoviesSearchScreenState.Error.UndefinedError(error.message)
                }
            }
        }

        isNextPageLoading = false
    }

    private fun handleSuccess(resource: Resource.Success<MoviesSearchResult>) {
        pagesTotal = resource.data.pages
        currentPage = resource.data.currentPage
        movies.addAll(resource.data.movies)

        _uiState.value = if (movies.isEmpty()) {
            MoviesSearchScreenState.EmptyResult
        } else {
            MoviesSearchScreenState.Content(
                movies = movies.toList()
            )
        }

        isNextPageLoading = false
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_IN_MILLIS = 1000L
    }

    sealed interface SearchMode {
        data object AllMovies : SearchMode
        data object SearchByTitle : SearchMode

        data object SearchByParams : SearchMode
    }
}