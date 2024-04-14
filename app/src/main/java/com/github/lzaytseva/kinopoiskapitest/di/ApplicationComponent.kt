package com.github.lzaytseva.kinopoiskapitest.di

import android.content.Context
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.ViewModelFactory
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        DomainModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {

    fun getViewModelFactory(): ViewModelFactory

    fun getMovieDetailsComponentFactory(): MovieDetailsComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}