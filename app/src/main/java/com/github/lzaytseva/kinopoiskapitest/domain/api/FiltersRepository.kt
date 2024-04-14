package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.FilterValue
import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow

interface FiltersRepository {

    fun getCountries(): Flow<Resource<List<FilterValue>>>

    fun getGenres(): Flow<Resource<List<FilterValue>>>

    suspend fun preloadValues()

    fun getFilters(): Flow<Filters>

    fun saveFilters(filters: Filters)

    fun clearFilters()
}