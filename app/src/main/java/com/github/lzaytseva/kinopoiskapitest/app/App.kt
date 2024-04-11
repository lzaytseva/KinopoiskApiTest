package com.github.lzaytseva.kinopoiskapitest.app

import android.app.Application
import com.github.lzaytseva.kinopoiskapitest.di.ApplicationComponent
import com.github.lzaytseva.kinopoiskapitest.di.DaggerApplicationComponent

class App : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.factory().create(
            context = this
        )
    }
}