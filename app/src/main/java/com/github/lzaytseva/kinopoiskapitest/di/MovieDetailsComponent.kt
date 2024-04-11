package com.github.lzaytseva.kinopoiskapitest.di

import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MovieDetailsViewModelModule::class
    ]
)
interface MovieDetailsComponent {


    fun getViewModelFactory(): ViewModelFactory

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance movieId: Int
        ): MovieDetailsComponent
    }
}