package com.github.lzaytseva.kinopoiskapitest.data.network.api

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.CountriesResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.GenresResponse

interface FiltersRemoteDataSource {

    suspend fun getCountries(): CountriesResponse

    suspend fun getGenres(): GenresResponse
}