package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow


interface MoviesSearchByParamsRepository {

    fun searchMoviesByParams(limit: Int): Flow<Resource<MoviesSearchResult>>
}