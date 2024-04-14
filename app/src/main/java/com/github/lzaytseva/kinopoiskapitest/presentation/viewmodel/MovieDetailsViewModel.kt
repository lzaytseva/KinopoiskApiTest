package com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.ErrorEntity
import com.github.lzaytseva.kinopoiskapitest.domain.api.MovieDetailsInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MovieDetailsScreenState
import com.github.lzaytseva.kinopoiskapitest.util.BaseViewModel
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val movieId: Int,
    private val interactor: MovieDetailsInteractor
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<MovieDetailsScreenState> =
        MutableStateFlow(MovieDetailsScreenState.Initial)
    val uiState: StateFlow<MovieDetailsScreenState> = _uiState.asStateFlow()

    init {
        getMovieDetails()
    }

    private fun getMovieDetails() {
        viewModelScope.launch {
            _uiState.value = MovieDetailsScreenState.Loading

            interactor.getMovieDetails(movieId).collect {
                processResult(it)
            }
        }
    }

    private fun processResult(resource: Resource<MovieDetails>) {
        when (resource) {
            is Resource.Error -> {
                handleError(resource.error)
            }

            is Resource.Success -> {
                _uiState.value = MovieDetailsScreenState.Content(details = resource.data)
            }
        }
    }

    private fun handleError(error: ErrorEntity) {
        _uiState.value = when (error) {
            ErrorEntity.NoInternet -> {
                MovieDetailsScreenState.Error.NoInternet
            }

            ErrorEntity.ServerError -> {
                MovieDetailsScreenState.Error.ServerError
            }

            is ErrorEntity.UndefinedError -> {
                MovieDetailsScreenState.Error.UndefinedError(error.message)
            }
        }
    }
}