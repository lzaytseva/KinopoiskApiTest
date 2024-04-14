package com.github.lzaytseva.kinopoiskapitest.data.repository

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.MoviesExceptionToErrorEntityMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.MoviesException
import com.github.lzaytseva.kinopoiskapitest.data.mapper.MovieMapper
import com.github.lzaytseva.kinopoiskapitest.data.network.api.SearchMoviesRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetAllMoviesRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchByParamsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.SearchByTitleRequest
import com.github.lzaytseva.kinopoiskapitest.data.storage.FiltersStorage
import com.github.lzaytseva.kinopoiskapitest.domain.api.SearchMoviesRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchMoviesRepositoryImpl @Inject constructor(
    private val remoteDataSource: SearchMoviesRemoteDataSource,
    private val mapper: MovieMapper,
    private val moviesExceptionToErrorEntity: MoviesExceptionToErrorEntityMapper,
    private val filtersStorage: FiltersStorage
) : SearchMoviesRepository {

    private var limit: Int? = null
    private var nextPage: Int = 1
    private var searchQuery: String? = null
    override fun searchMoviesByTitle(
        searchQuery: String,
        limit: Int,
        page: Int
    ): Flow<Resource<MoviesSearchResult>> {
        this.limit = null

        return flow {
            try {
                val response = remoteDataSource.searchMoviesByTitle(
                    request = SearchByTitleRequest(
                        searchQuery = searchQuery,
                        page = page,
                        limit = limit
                    )
                )

                nextPage = response.page + 1
                this@SearchMoviesRepositoryImpl.limit = limit
                this@SearchMoviesRepositoryImpl.searchQuery = searchQuery

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

    override fun loadSearchByTitleNextPage(): Flow<Resource<MoviesSearchResult>> {
        val searchQuery = this.searchQuery
        val limit = this.limit

        return if (searchQuery != null && limit != null) {
            searchMoviesByTitle(
                searchQuery = searchQuery,
                limit = limit,
                page = nextPage
            )
        } else {
            throw RuntimeException("loadNextPage call before got first results")
        }
    }

    override fun getAllMovies(limit: Int, page: Int): Flow<Resource<MoviesSearchResult>> {
        this.limit = null

        return flow {
            try {
                val response = remoteDataSource.getAllMovies(
                    request = GetAllMoviesRequest(
                        page = page,
                        limit = limit
                    )
                )

                nextPage = response.page + 1
                this@SearchMoviesRepositoryImpl.limit = limit

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

    override fun loadAllMoviesNextPage(): Flow<Resource<MoviesSearchResult>> {
        return limit?.let {
            getAllMovies(
                limit = it,
                page = nextPage
            )
        }
            ?: throw RuntimeException("loadNextPage call before got first results")
    }


    override fun searchMoviesByParams(limit: Int, page: Int): Flow<Resource<MoviesSearchResult>> {
        this.limit = null
        val filters = filtersStorage.get()

        return flow {
            try {
                val response = remoteDataSource.searchMoviesByParams(
                    request = SearchByParamsRequest(
                        year = filters.year,
                        ageRating = filters.ageRating,
                        country = filters.country,
                        ratingKp = filters.ratingKp,
                        type = filters.type,
                        page = page,
                        limit = limit
                    )
                )

                nextPage = response.page + 1
                this@SearchMoviesRepositoryImpl.limit = limit

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

    override fun loadSearchByParamsNextPage(): Flow<Resource<MoviesSearchResult>> {
        return limit?.let {
            searchMoviesByParams(
                limit = it,
                page = nextPage
            )
        }
            ?: throw RuntimeException("loadNextPage call before got first results")
    }
}