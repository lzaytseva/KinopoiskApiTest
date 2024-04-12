package com.github.lzaytseva.kinopoiskapitest.di


import com.github.lzaytseva.kinopoiskapitest.data.repository.SearchMoviesRepositoryImpl
import com.github.lzaytseva.kinopoiskapitest.domain.api.SearchMoviesRepository
import dagger.Binds
import dagger.Module


@Module
interface RepositoryModule {
    @[Binds ApplicationScope]
    fun bindSearchMoviesRepository(
        impl: SearchMoviesRepositoryImpl
    ): SearchMoviesRepository
}