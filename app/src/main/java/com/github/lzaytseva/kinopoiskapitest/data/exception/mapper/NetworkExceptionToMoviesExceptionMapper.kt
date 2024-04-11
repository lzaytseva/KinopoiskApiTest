package com.github.lzaytseva.kinopoiskapitest.data.exception.mapper

import com.github.lzaytseva.kinopoiskapitest.data.exception.model.MoviesException
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.NetworkException
import javax.inject.Inject

class NetworkExceptionToMoviesExceptionMapper @Inject constructor() {

    fun handleException(exception: NetworkException): MoviesException {
        return when (exception) {
            is NetworkException.InternalServerError -> {
                MoviesException.InternalSeverError(exception.errorBody)
            }

            is NetworkException.NoInternetConnection -> {
                MoviesException.NoConnection(exception.errorBody)
            }

            else -> {
                MoviesException.Other(
                    message = "Code: ${exception.httpStatusCode} \n Message: ${exception.errorBody}"
                )
            }
        }
    }
}