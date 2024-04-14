package com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel

sealed interface SearchMode {
    data object AllMovies : SearchMode
    data object SearchByTitle : SearchMode

    data object SearchByParams : SearchMode
}