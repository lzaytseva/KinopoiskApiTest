package com.github.lzaytseva.kinopoiskapitest.data.exception.interceptor

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.NetworkErrorCodeToExceptionMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.NetworkException
import com.github.lzaytseva.kinopoiskapitest.util.NetworkConnectionChecker
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ErrorInterceptor @Inject constructor(
    private val networkErrorCodeToExceptionMapper: NetworkErrorCodeToExceptionMapper,
    private val networkConnectionChecker: NetworkConnectionChecker,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if (!networkConnectionChecker.isConnected()) {
            throw NetworkException.NoInternetConnection()
        }
        val request = chain.request()
        val response = chain.proceed(request)
        if (response.code !in (HttpURLConnection.HTTP_OK..HttpURLConnection.HTTP_CREATED)) {
            val responseBody = response.body
            if (responseBody == null) {
                throw NetworkException.ResponseBodyIsNull(httpStatusCode = response.code)
            } else {
                throw getExceptionAccordingToResponseCode(responseBody.string(), response.code)
            }
        }
        return response
    }

    private fun getExceptionAccordingToResponseCode(
        errorMessage: String, responseCode: Int,
    ): IOException {
        throw networkErrorCodeToExceptionMapper.getException(
            errorMessage = errorMessage, responseCode = responseCode
        )
    }
}