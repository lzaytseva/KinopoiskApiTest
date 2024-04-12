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
                    queryParams = request.toQueryMap()
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

    private fun SearchByParamsRequest.toQueryMap(): Map<String, String> = buildMap {
        put(PAGE, page.toString())
        put(LIMIT, limit.toString())
        year?.also { put(YEAR, it) }
        ageRating?.also { put(AGE_RATING, it) }
        country?.also { put(COUNTRY_NAME, it) }
    }

    private companion object {
        const val PAGE = "page"
        const val LIMIT = "limit"
        const val YEAR = "year"
        const val AGE_RATING = "ageRating"
        const val COUNTRY_NAME = "countries.name"
    }
}