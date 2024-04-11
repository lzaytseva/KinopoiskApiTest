package com.github.lzaytseva.kinopoiskapitest.util

import com.github.lzaytseva.kinopoiskapitest.data.exception.model.ErrorEntity

sealed class Resource<T> {

    class Success<T>(val data: T) : Resource<T>()

    class Error<T>(val error: ErrorEntity) : Resource<T>()
}