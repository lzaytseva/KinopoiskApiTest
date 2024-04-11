package com.github.lzaytseva.kinopoiskapitest.di

import com.github.lzaytseva.kinopoiskapitest.domain.api.MoviesSearchByParamsInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.impl.MoviesSearchByParamsInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @[Binds ApplicationScope]
    fun bindMoviesSearchByParamsInteractor(
        impl: MoviesSearchByParamsInteractorImpl
    ): MoviesSearchByParamsInteractor
}