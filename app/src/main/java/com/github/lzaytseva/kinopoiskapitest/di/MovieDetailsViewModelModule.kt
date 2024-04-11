package com.github.lzaytseva.kinopoiskapitest.di

import androidx.lifecycle.ViewModel
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.MovieDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MovieDetailsViewModelModule {

    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    @Binds
    fun bindMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel
}