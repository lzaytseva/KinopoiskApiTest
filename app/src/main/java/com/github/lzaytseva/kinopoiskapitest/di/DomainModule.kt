package com.github.lzaytseva.kinopoiskapitest.di

import com.github.lzaytseva.kinopoiskapitest.domain.api.SearchMovieInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.impl.SearchMovieInteractorImpl
import dagger.Binds
import dagger.Module

@Module
interface DomainModule {

    @Binds
    fun bindSearchMovieInteractor(
        impl: SearchMovieInteractorImpl
    ): SearchMovieInteractor
}