package com.github.lzaytseva.kinopoiskapitest.data.exception.model

import java.io.IOException

sealed class NetworkException(
    val errorBody: String,
    val httpStatusCode: Int
) : IOException(errorBody) {

    class Forbidden(errorMessage: String, httpStatusCode: Int) :
        NetworkException(errorMessage, httpStatusCode)

    class Unauthorized(errorMessage: String, httpStatusCode: Int) :
        NetworkException(errorMessage, httpStatusCode)

    class NotFound(errorMessage: String, httpStatusCode: Int) :
        NetworkException(errorMessage, httpStatusCode)

    class InternalServerError(errorMessage: String, httpStatusCode: Int) :
        NetworkException(errorMessage, httpStatusCode)

    class NoInternetConnection(
        errorMessage: String = NO_INTERNET_CONNECTION,
        httpStatusCode: Int = NO_INTERNET_CONNECTION_CODE
    ) : NetworkException(errorMessage, httpStatusCode)

    class Undefined(
        errorMessage: String = UNDEFINED_MESSAGE,
        httpStatusCode: Int = UNDEFINED_CODE
    ) : NetworkException(errorMessage, httpStatusCode)

    class ResponseBodyIsNull(
        errorMessage: String = RESPONSE_BODY_IS_NULL,
        httpStatusCode: Int
    ) : NetworkException(errorMessage, httpStatusCode)


    private companion object {
        const val NO_INTERNET_CONNECTION = "No internet connection"
        const val NO_INTERNET_CONNECTION_CODE = -1
        const val RESPONSE_BODY_IS_NULL = "Response body is null"
        const val UNDEFINED_MESSAGE = "Undefined"
        const val UNDEFINED_CODE = 0
    }
}