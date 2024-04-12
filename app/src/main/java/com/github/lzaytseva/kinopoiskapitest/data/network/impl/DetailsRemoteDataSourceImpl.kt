package com.github.lzaytseva.kinopoiskapitest.data.network.impl

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.NetworkExceptionToMoviesExceptionMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.NetworkException
import com.github.lzaytseva.kinopoiskapitest.data.network.api.DetailsRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.api.KinopoiskApiService
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetImagesRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetMovieDetailsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetReviewsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetSeasonsInfoRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ImagesResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MovieDetailsResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ReviewsResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.SeasonsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailsRemoteDataSourceImpl @Inject constructor(
    private val apiService: KinopoiskApiService,
    private val networkExceptionToMoviesExceptionMapper: NetworkExceptionToMoviesExceptionMapper
) : DetailsRemoteDataSource {

    override suspend fun getMovieDetails(request: GetMovieDetailsRequest): MovieDetailsResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getMovieDetails(
                    movieId = request.movieId
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

    override suspend fun getSeasonsInfo(request: GetSeasonsInfoRequest): SeasonsResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getSeasonsInfo(
                    movieId = request.movieId,
                    page = request.page,
                    limit = request.limit
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

    override suspend fun getImages(request: GetImagesRequest): ImagesResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getImages(
                    movieId = request.movieId,
                    page = request.page,
                    limit = request.limit
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }

    override suspend fun getReviews(request: GetReviewsRequest): ReviewsResponse {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getReviews(
                    movieId = request.movieId,
                    page = request.page,
                    limit = request.limit
                )
            } catch (exception: NetworkException) {
                throw networkExceptionToMoviesExceptionMapper.handleException(exception)
            }
        }
    }
}
