package com.github.lzaytseva.kinopoiskapitest.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        RepositoryModule::class,
        DomainModule::class,
        ViewModelModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}