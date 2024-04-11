package com.github.lzaytseva.kinopoiskapitest.data.network.api

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchMovieByParamsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.SearchMovieByParamsResponse

interface SearchMoviesRemoteDataSource {

    suspend fun searchMoviesByParams(
        request: SearchMovieByParamsRequest
    ): SearchMovieByParamsResponse
}