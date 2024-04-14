package com.github.lzaytseva.kinopoiskapitest.di

import androidx.lifecycle.ViewModel
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.FiltersViewModel
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.SearchMoviesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(SearchMoviesViewModel::class)
    @Binds
    fun bindSearchMoviesViewModel(viewModel: SearchMoviesViewModel): ViewModel

    @IntoMap
    @ViewModelKey(FiltersViewModel::class)
    @Binds
    fun bindFiltersViewModel(viewModel: FiltersViewModel): ViewModel
}