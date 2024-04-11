package com.github.lzaytseva.kinopoiskapitest.util

sealed class Resource<T> {

    class Success<T>(val data: T) : Resource<T>()

    // TODO: don't forget to replace String with custom error type
    class Error<T>(val error: String) : Resource<T>()
}