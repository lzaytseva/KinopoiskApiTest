package com.github.lzaytseva.kinopoiskapitest.presentation.state

import com.github.lzaytseva.kinopoiskapitest.domain.model.Movie

sealed interface MoviesSearchScreenState {

    data object Initial : MoviesSearchScreenState

    data object Loading : MoviesSearchScreenState

    data class Content(val movies: List<Movie>) : MoviesSearchScreenState

    data object LoadingNextPage : MoviesSearchScreenState

    data object EmptyResult : MoviesSearchScreenState

    sealed interface Error : MoviesSearchScreenState {
        data object NoInternet : Error

        data object ServerError : Error

        data class UndefinedError(val error: String) : Error
    }
}