package com.github.lzaytseva.kinopoiskapitest.data.exception.model

sealed interface ErrorEntity {

    data object NoInternet : ErrorEntity

    data object ServerError : ErrorEntity

    data class UndefinedError(val message: String) : ErrorEntity
}