package com.github.lzaytseva.kinopoiskapitest.domain.impl

import com.github.lzaytseva.kinopoiskapitest.domain.api.MovieDetailsInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.api.MovieDetailsRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieDetailsInteractorImpl @Inject constructor(
    private val repository: MovieDetailsRepository
) : MovieDetailsInteractor {
    override suspend fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetails>> {
        return repository.getMovieDetails(movieId)
    }
}