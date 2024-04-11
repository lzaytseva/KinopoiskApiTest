package com.github.lzaytseva.kinopoiskapitest.di


import com.github.lzaytseva.kinopoiskapitest.data.repository.MoviesSearchByParamsRepositoryImpl
import com.github.lzaytseva.kinopoiskapitest.domain.api.MoviesSearchByParamsRepository
import dagger.Binds
import dagger.Module


@Module
interface RepositoryModule {

    @[Binds ApplicationScope]
    fun bindMoviesSearchByParamsRepository(
        impl: MoviesSearchByParamsRepositoryImpl
    ): MoviesSearchByParamsRepository
}