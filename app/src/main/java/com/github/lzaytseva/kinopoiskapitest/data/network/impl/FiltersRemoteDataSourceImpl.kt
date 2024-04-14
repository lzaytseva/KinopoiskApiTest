package com.github.lzaytseva.kinopoiskapitest.data.network.impl

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.NetworkExceptionToMoviesExceptionMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.NetworkException
import com.github.lzaytseva.kinopoiskapitest.data.network.api.FiltersRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.api.KinopoiskApiService
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.CountriesResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.GenresResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FiltersRemoteDataSourceImpl @Inject constructor(
    private val apiService: KinopoiskApiService,
    private val networkExceptionToMoviesExceptionMapper: NetworkExceptionToMoviesExceptionMapper
) : FiltersRemoteDataSource {
    override suspend fun getCountries(): CountriesResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getCountries()
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

    override suspend fun getGenres(): GenresResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getGenres()
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }
}