package com.github.lzaytseva.kinopoiskapitest.domain.impl

import com.github.lzaytseva.kinopoiskapitest.domain.api.FiltersInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.api.FiltersRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.FilterValue
import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FiltersInteractorImpl @Inject constructor(
    private val repository: FiltersRepository
) : FiltersInteractor {
    override fun getCountries(): Flow<Resource<List<FilterValue>>> {
        return repository.getCountries()
    }

    override fun getGenres(): Flow<Resource<List<FilterValue>>> {
        return repository.getGenres()
    }

    override suspend fun preloadValues() {
        return repository.preloadValues()
    }

    override fun getFilters(): Flow<Filters> {
        return repository.getFilters()
    }

    override fun saveFilters(filters: Filters) {
        repository.saveFilters(filters)
    }

    override fun clearFilters() {
        repository.clearFilters()
    }
}