package com.github.lzaytseva.kinopoiskapitest.presentation.state

sealed interface MoviesSearchSideEffect {

    data object NoInternet : MoviesSearchSideEffect

    data object ServerError : MoviesSearchSideEffect

    data class UndefinedError(val error: String) : MoviesSearchSideEffect
}