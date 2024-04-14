package com.github.lzaytseva.kinopoiskapitest.presentation.state

import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails

sealed interface MovieDetailsScreenState {

    data object Initial : MovieDetailsScreenState

    data object Loading : MovieDetailsScreenState

    data class Content(val details: MovieDetails) : MovieDetailsScreenState

    sealed interface Error : MovieDetailsScreenState {
        data object NoInternet : Error

        data object ServerError : Error

        data class UndefinedError(val error: String) : Error
    }
}