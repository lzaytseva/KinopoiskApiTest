package com.github.lzaytseva.kinopoiskapitest.data.exception.mapper

import com.github.lzaytseva.kinopoiskapitest.data.exception.model.ErrorEntity
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.MoviesException
import javax.inject.Inject

class MoviesExceptionToErrorEntityMapper @Inject constructor() {

    fun handleException(exception: MoviesException): ErrorEntity {
        return when (exception) {
            is MoviesException.InternalSeverError -> ErrorEntity.ServerError
            is MoviesException.NoConnection -> ErrorEntity.NoInternet
            is MoviesException.Other -> ErrorEntity.UndefinedError(exception.message)
        }
    }
}