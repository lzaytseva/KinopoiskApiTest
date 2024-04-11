package com.github.lzaytseva.kinopoiskapitest.data.exception.mapper

import com.github.lzaytseva.kinopoiskapitest.data.exception.model.NetworkException
import java.io.IOException
import javax.inject.Inject

class NetworkErrorCodeToExceptionMapper @Inject constructor() {

    fun getException(errorMessage: String, responseCode: Int): IOException {
        return when (responseCode) {
            401 -> NetworkException.Unauthorized(errorMessage, responseCode)
            403 -> NetworkException.Forbidden(errorMessage, responseCode)
            404 -> NetworkException.NotFound(errorMessage, responseCode)
            500 -> NetworkException.InternalServerError(errorMessage, responseCode)
            else -> NetworkException.Undefined(errorMessage, responseCode)
        }
    }
}