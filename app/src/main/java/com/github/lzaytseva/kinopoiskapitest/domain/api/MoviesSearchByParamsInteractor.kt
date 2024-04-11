package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow


interface MoviesSearchByParamsInteractor {

    fun searchMoviesByParams(limitPerPage: Int = 10): Flow<Resource<MoviesSearchResult>>

    fun loadNextPage(): Flow<Resource<MoviesSearchResult>>
}