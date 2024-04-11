package com.github.lzaytseva.kinopoiskapitest.domain.model

data class MoviesSearchResult(
    val pages: Int,
    val movies: List<Movie>
)