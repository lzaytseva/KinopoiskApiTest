package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieDetailsInteractor {

    fun getMovieDetails(
        movieId: Int
    ): Flow<Resource<MovieDetails>>

}