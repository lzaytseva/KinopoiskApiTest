package com.github.lzaytseva.kinopoiskapitest.data.network.api

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ImagesResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MovieDetailsResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MoviesResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ReviewsResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.SeasonsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface KinopoiskApiService {

    @GET("/v1.4/movie")
    suspend fun searchMoviesByParams(
        @QueryMap queryParams: Map<String, String>
    ): MoviesResponse

    @GET("/v1.4/movie")
    suspend fun getAllMovies(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): MoviesResponse

    @GET("/v1.4/movie/search")
    suspend fun searchMoviesByTitle(
        @Query("query") searchQuery: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): MoviesResponse

    @GET("v1.4/movie/{id}")
    suspend fun getMovieDetails(@Path("id") movieId: Int): MovieDetailsResponse

    @GET("/v1.4/season")
    suspend fun getSeasonsInfo(
        @Query("movieId") movieId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): SeasonsResponse

    @GET("/v1.4/image")
    suspend fun getImages(
        @Query("movieId") movieId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ImagesResponse

    @GET("v1.4/review")
    suspend fun getReviews(
        @Query("movieId") movieId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): ReviewsResponse
}