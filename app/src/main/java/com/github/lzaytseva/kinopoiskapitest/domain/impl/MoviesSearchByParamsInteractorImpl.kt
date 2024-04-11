package com.github.lzaytseva.kinopoiskapitest.domain.impl

import com.github.lzaytseva.kinopoiskapitest.domain.api.MoviesSearchByParamsInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.api.MoviesSearchByParamsRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesSearchByParamsInteractorImpl @Inject constructor(
    private val repository: MoviesSearchByParamsRepository
) : MoviesSearchByParamsInteractor {

    override fun searchMoviesByParams(limitPerPage: Int): Flow<Resource<MoviesSearchResult>> {
        return repository.searchMoviesByParams(limitPerPage)
    }

    override fun loadNextPage(): Flow<Resource<MoviesSearchResult>> {
        return repository.loadNextPage()
    }
}