package com.github.lzaytseva.kinopoiskapitest.domain.api

import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieDetailsRepository {

    fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetails>>
}