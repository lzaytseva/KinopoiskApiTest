package com.github.lzaytseva.kinopoiskapitest.data.exception.model

import java.io.IOException

sealed class MoviesException(
    override val message: String
) : IOException(message) {

    data class NoConnection(
        override val message: String
    ) : MoviesException(message)

    data class Other(
        override val message: String
    ) : MoviesException(message)

    data class InternalSeverError(
        override val message: String
    ) : MoviesException(message)
}