package com.github.lzaytseva.kinopoiskapitest.di


import android.content.Context
import com.github.lzaytseva.kinopoiskapitest.BuildConfig
import com.github.lzaytseva.kinopoiskapitest.data.exception.interceptor.ErrorInterceptor
import com.github.lzaytseva.kinopoiskapitest.data.network.api.DetailsRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.api.FiltersRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.api.KinopoiskApiService
import com.github.lzaytseva.kinopoiskapitest.data.network.api.SearchMoviesRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.impl.DetailsRemoteDataSourceImpl
import com.github.lzaytseva.kinopoiskapitest.data.network.impl.FiltersRemoteDataSourceImpl
import com.github.lzaytseva.kinopoiskapitest.data.network.impl.SearchMoviesRemoteDataSourceImpl
import com.github.lzaytseva.kinopoiskapitest.util.NetworkConnectionChecker
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
interface NetworkModule {

    @[Binds ApplicationScope]
    fun bindSearchMoviesRemoteDataSource(
        impl: SearchMoviesRemoteDataSourceImpl
    ): SearchMoviesRemoteDataSource

    @[Binds ApplicationScope]
    fun bindDetailsRemoteDataSource(
        impl: DetailsRemoteDataSourceImpl
    ): DetailsRemoteDataSource

    @[Binds ApplicationScope]
    fun bindFiltersRemoteDataSource(
        impl: FiltersRemoteDataSourceImpl
    ): FiltersRemoteDataSource


    companion object {

        @[Provides ApplicationScope]
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @[Provides ApplicationScope]
        fun provideNetworkConnectionChecker(context: Context): NetworkConnectionChecker {
            return NetworkConnectionChecker(context)
        }

        @[Provides ApplicationScope]
        fun provideApiService(
            httpLoggingInterceptor: HttpLoggingInterceptor,
            errorInterceptor: ErrorInterceptor
        ): KinopoiskApiService {
            val client = OkHttpClient()
                .newBuilder()
                .addInterceptor { chain ->
                    chain.run {
                        proceed(
                            request()
                                .newBuilder()
                                .addHeader("X-API-KEY", BuildConfig.KINOPOISK_API_KEY)
                                .build()
                        )
                    }
                }
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(errorInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KinopoiskApiService::class.java)
        }

        private const val BASE_URL = "https://api.kinopoisk.dev/"
    }
}