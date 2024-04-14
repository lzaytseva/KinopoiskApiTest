package com.github.lzaytseva.kinopoiskapitest.data.repository

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.MoviesExceptionToErrorEntityMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.MoviesException
import com.github.lzaytseva.kinopoiskapitest.data.network.api.FiltersRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.storage.FiltersStorage
import com.github.lzaytseva.kinopoiskapitest.domain.api.FiltersRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.FilterValue
import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FiltersRepositoryImpl @Inject constructor(
    private val remoteDataSource: FiltersRemoteDataSource,
    private val moviesExceptionToErrorEntity: MoviesExceptionToErrorEntityMapper,
    private val storage: FiltersStorage
) : FiltersRepository {

    private val loadedCountries = mutableListOf<FilterValue>()
    private val loadedGenres = mutableListOf<FilterValue>()

    override fun getCountries(): Flow<Resource<List<FilterValue>>> {
        return flow {

            if (loadedCountries.isNotEmpty()) {
                emit(
                    Resource.Success(
                        data = loadedCountries.toList()
                    )
                )
                return@flow
            }

            try {
                val response = remoteDataSource.getCountries()
                val countries = response
                    .mapNotNull { it.name }
                    .map { FilterValue(it, type = FilterValue.FilterType.Country) }
                loadedCountries.addAll(countries)

                emit(
                    Resource.Success(
                        data = countries
                    )
                )
            } catch (exception: MoviesException) {
                emit(
                    Resource.Error(
                        error = moviesExceptionToErrorEntity.handleException(exception)
                    )
                )
            }
        }
    }

    override fun getGenres(): Flow<Resource<List<FilterValue>>> {
        return flow {
            if (loadedGenres.isNotEmpty()) {
                emit(
                    Resource.Success(
                        data = loadedGenres.toList()
                    )
                )
                return@flow
            }

            try {
                val response = remoteDataSource.getGenres()
                val genres = response
                    .mapNotNull { it.name }
                    .map { FilterValue(it, type = FilterValue.FilterType.Genre) }
                loadedGenres.addAll(genres)

                emit(
                    Resource.Success(
                        data = genres
                    )
                )
            } catch (exception: MoviesException) {
                emit(
                    Resource.Error(
                        error = moviesExceptionToErrorEntity.handleException(exception)
                    )
                )
            }
        }
    }

    override suspend fun preloadValues() {
        try {
            val response = remoteDataSource.getCountries()
            val countries = response
                .mapNotNull { it.name }
                .map { FilterValue(it, type = FilterValue.FilterType.Country) }
            loadedCountries.addAll(countries)

        } catch (_: MoviesException) {
            // список просто останется пустым до след попытки загрузки
        }
        try {
            val response = remoteDataSource.getCountries()
            val countries = response
                .mapNotNull { it.name }
                .map { FilterValue(it, type = FilterValue.FilterType.Genre) }
            loadedCountries.addAll(countries)

        } catch (_: MoviesException) {

        }
    }

    override fun getFilters(): Flow<Filters> {
        return flow {
            emit(storage.get())
        }
    }

    override fun saveFilters(filters: Filters) {
        storage.save(filters)
    }

    override fun clearFilters() {
        storage.clear()
    }
}