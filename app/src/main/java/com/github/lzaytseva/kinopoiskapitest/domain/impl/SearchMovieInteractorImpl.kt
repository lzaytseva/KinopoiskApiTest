package com.github.lzaytseva.kinopoiskapitest.domain.impl

import com.github.lzaytseva.kinopoiskapitest.domain.api.SearchMovieInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.api.SearchMoviesRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMovieInteractorImpl @Inject constructor(
    private val repository: SearchMoviesRepository
) : SearchMovieInteractor {
    override fun searchMoviesByTitle(
        searchQuery: String,
        limitPerPage: Int
    ): Flow<Resource<MoviesSearchResult>> {
        return repository.searchMoviesByTitle(searchQuery, limitPerPage)
    }

    override fun loadSearchByTitleNextPage(): Flow<Resource<MoviesSearchResult>> {
        return repository.loadSearchByTitleNextPage()
    }

    override fun getAllMovies(limitPerPage: Int): Flow<Resource<MoviesSearchResult>> {
        return repository.getAllMovies(limitPerPage)
    }

    override fun loadAllMoviesNextPage(): Flow<Resource<MoviesSearchResult>> {
        return repository.loadAllMoviesNextPage()
    }

    override fun searchMoviesByParams(limitPerPage: Int): Flow<Resource<MoviesSearchResult>> {
        return repository.searchMoviesByParams(limitPerPage)
    }

    override fun loadSearchByParamsNextPage(): Flow<Resource<MoviesSearchResult>> {
        return repository.loadSearchByParamsNextPage()
    }
}