package com.github.lzaytseva.kinopoiskapitest.data.repository

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.MoviesExceptionToErrorEntityMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.MoviesException
import com.github.lzaytseva.kinopoiskapitest.data.mapper.MovieMapper
import com.github.lzaytseva.kinopoiskapitest.data.network.api.SearchMoviesRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchMovieByParamsRequest
import com.github.lzaytseva.kinopoiskapitest.domain.api.MoviesSearchByParamsRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesSearchByParamsRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchMoviesRemoteDataSource,
    private val mapper: MovieMapper,
    private val moviesExceptionToErrorEntity: MoviesExceptionToErrorEntityMapper,
) : MoviesSearchByParamsRepository {

    private var nextPage: Int = 1

    override fun searchMoviesByParams(limit: Int): Flow<Resource<MoviesSearchResult>> {
        return flow {
            try {
                val response = remoteDataSource.searchMoviesByParams(
                    // Пока фильтры не реализованы, выгружаем все фильмы
                    request = SearchMovieByParamsRequest(
                        year = null,
                        ageRating = null,
                        country = null,
                        page = nextPage,
                        limit = limit
                    )
                )

                nextPage++

                emit(
                    Resource.Success(
                        data = mapper.searchMovieResponseToMoviesResult(response)
                    )
                )
            } catch (exception: MoviesException) {
                emit(
                    Resource.Error(
                        error = moviesExceptionToErrorEntity.handleException(exception)
                    )
                )
            }
        }
    }
}