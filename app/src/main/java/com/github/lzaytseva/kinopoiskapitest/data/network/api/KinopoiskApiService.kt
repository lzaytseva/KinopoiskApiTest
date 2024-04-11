package com.github.lzaytseva.kinopoiskapitest.data.network.api

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.SearchMoviesByParamsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface KinopoiskApiService {

    @GET("/v1.4/movie")
    suspend fun searchMoviesByParams(
        @QueryMap queryParams: Map<String, String>
    ): SearchMoviesByParamsResponse

    @GET("/v1.4/movie/search")
    suspend fun searchMoviesByTitle(
        @Query("query") searchQuery: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    )

    @GET("v1.4/movie/{id}")
    suspend fun getMovieDetails(@Path("id") movieId: Int)
}