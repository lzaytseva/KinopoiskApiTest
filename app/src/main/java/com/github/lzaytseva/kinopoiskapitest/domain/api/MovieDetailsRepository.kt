package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.ImagesResult
import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.domain.model.ReviewsResult
import com.github.lzaytseva.kinopoiskapitest.domain.model.SeasonsResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {

    fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetails>>

    fun getSeasonsInfo(
        movieId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<SeasonsResult>>

    fun getImages(
        movieId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<ImagesResult>>

    fun getReviews(
        movieId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<ReviewsResult>>

}