package com.github.lzaytseva.kinopoiskapitest.data.network.impl

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.NetworkExceptionToMoviesExceptionMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.NetworkException
import com.github.lzaytseva.kinopoiskapitest.data.network.api.KinopoiskApiService
import com.github.lzaytseva.kinopoiskapitest.data.network.api.SearchMoviesRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchMovieByParamsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.SearchMoviesByParamsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchMoviesRemoteDataSourceImpl @Inject constructor(
    private val apiService: KinopoiskApiService,
    private val networkExceptionToMoviesExceptionMapper: NetworkExceptionToMoviesExceptionMapper
) : SearchMoviesRemoteDataSource {

    override suspend fun searchMoviesByParams(
        request: SearchMovieByParamsRequest
    ): SearchMoviesByParamsResponse {
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

    private fun SearchMovieByParamsRequest.toQueryMap(): Map<String, String> = buildMap {
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