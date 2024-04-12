package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchMovieInteractor {

    fun searchMoviesByTitle(
        searchQuery: String,
        limitPerPage: Int = 20
    ): Flow<Resource<MoviesSearchResult>>

    fun loadSearchByTitleNextPage(): Flow<Resource<MoviesSearchResult>>

    fun getAllMovies(limitPerPage: Int = 20): Flow<Resource<MoviesSearchResult>>

    fun loadAllMoviesNextPage(): Flow<Resource<MoviesSearchResult>>

    fun searchMoviesByParams(limitPerPage: Int = 20): Flow<Resource<MoviesSearchResult>>

    fun loadSearchByParamsNextPage(): Flow<Resource<MoviesSearchResult>>
}