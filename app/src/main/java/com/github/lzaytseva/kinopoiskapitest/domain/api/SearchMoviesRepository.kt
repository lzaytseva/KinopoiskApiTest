package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow

interface SearchMoviesRepository {

    fun searchMoviesByTitle(
        searchQuery: String,
        limit: Int,
        page: Int = 1
    ): Flow<Resource<MoviesSearchResult>>

    fun loadSearchByTitleNextPage(): Flow<Resource<MoviesSearchResult>>

    fun getAllMovies(limit: Int, page: Int = 1): Flow<Resource<MoviesSearchResult>>

    fun loadAllMoviesNextPage(): Flow<Resource<MoviesSearchResult>>

    fun searchMoviesByParams(limit: Int, page: Int = 1): Flow<Resource<MoviesSearchResult>>

    fun loadSearchByParamsNextPage(): Flow<Resource<MoviesSearchResult>>
}