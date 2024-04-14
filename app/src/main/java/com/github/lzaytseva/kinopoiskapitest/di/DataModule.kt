package com.github.lzaytseva.kinopoiskapitest.di


import com.github.lzaytseva.kinopoiskapitest.data.repository.FiltersRepositoryImpl
import com.github.lzaytseva.kinopoiskapitest.data.repository.MovieDetailsRepositoryImpl
import com.github.lzaytseva.kinopoiskapitest.data.repository.SearchMoviesRepositoryImpl
import com.github.lzaytseva.kinopoiskapitest.data.storage.FiltersStorage
import com.github.lzaytseva.kinopoiskapitest.data.storage.FiltersStorageImpl
import com.github.lzaytseva.kinopoiskapitest.domain.api.FiltersRepository
import com.github.lzaytseva.kinopoiskapitest.domain.api.MovieDetailsRepository
import com.github.lzaytseva.kinopoiskapitest.domain.api.SearchMoviesRepository
import dagger.Binds
import dagger.Module


@Module
interface DataModule {
    @[Binds ApplicationScope]
    fun bindSearchMoviesRepository(
        impl: SearchMoviesRepositoryImpl
    ): SearchMoviesRepository

    @[Binds ApplicationScope]
    fun bindMovieDetailsRepository(
        impl: MovieDetailsRepositoryImpl
    ): MovieDetailsRepository

    @[Binds ApplicationScope]
    fun bindFiltersRepository(
        impl: FiltersRepositoryImpl
    ): FiltersRepository

    @[Binds ApplicationScope]
    fun bindFiltersStorage(
        impl: FiltersStorageImpl
    ): FiltersStorage
}