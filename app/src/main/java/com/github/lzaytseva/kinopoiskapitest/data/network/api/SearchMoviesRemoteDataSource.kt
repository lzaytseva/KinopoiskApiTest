package com.github.lzaytseva.kinopoiskapitest.data.network.api

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetAllMoviesRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchByParamsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchByTitleRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MoviesResponse

interface SearchMoviesRemoteDataSource {

    suspend fun getAllMovies(
        request: GetAllMoviesRequest
    ): MoviesResponse

    suspend fun searchMoviesByParams(
        request: SearchByParamsRequest
    ): MoviesResponse

    suspend fun searchMoviesByTitle(
        request: SearchByTitleRequest
    ): MoviesResponse
}