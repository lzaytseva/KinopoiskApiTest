package com.github.lzaytseva.kinopoiskapitest.data.network.impl

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.NetworkExceptionToMoviesExceptionMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.NetworkException
import com.github.lzaytseva.kinopoiskapitest.data.network.api.KinopoiskApiService
import com.github.lzaytseva.kinopoiskapitest.data.network.api.SearchMoviesRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetAllMoviesRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchByParamsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchByTitleRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchMoviesRemoteDataSourceImpl @Inject constructor(
    private val apiService: KinopoiskApiService,
    private val networkExceptionToMoviesExceptionMapper: NetworkExceptionToMoviesExceptionMapper
) : SearchMoviesRemoteDataSource {
    override suspend fun getAllMovies(request: GetAllMoviesRequest): MoviesResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAllMovies(
                    page = request.page,
                    limit = request.limit
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

    override suspend fun searchMoviesByTitle(request: SearchByTitleRequest): MoviesResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.searchMoviesByTitle(
                    searchQuery = request.searchQuery,
                    page = request.page,
                    limit = request.limit
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

    override suspend fun searchMoviesByParams(
        request: SearchByParamsRequest
    ): MoviesResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.searchMoviesByParams(
                    page = request.page,
                    year = request.year,
                    ageRating = request.ageRating,
                    country = request.country,
                    ratingKp = request.ratingKp,
                    type = request.type,
                    limit = request.limit
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

}