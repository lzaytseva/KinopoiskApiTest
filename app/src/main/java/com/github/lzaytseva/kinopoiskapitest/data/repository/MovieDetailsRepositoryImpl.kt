package com.github.lzaytseva.kinopoiskapitest.data.repository

import com.github.lzaytseva.kinopoiskapitest.data.exception.mapper.MoviesExceptionToErrorEntityMapper
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.MoviesException
import com.github.lzaytseva.kinopoiskapitest.data.mapper.DetailsMapper
import com.github.lzaytseva.kinopoiskapitest.data.network.api.DetailsRemoteDataSource
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetImagesRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetMovieDetailsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetReviewsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ImagesResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ReviewsResponse
import com.github.lzaytseva.kinopoiskapitest.domain.api.MovieDetailsRepository
import com.github.lzaytseva.kinopoiskapitest.domain.model.ImagesResult
import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.domain.model.ReviewsResult
import com.github.lzaytseva.kinopoiskapitest.domain.model.SeasonsResult
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val remoteDataSource: DetailsRemoteDataSource,
    private val mapper: DetailsMapper,
    private val moviesExceptionToErrorEntity: MoviesExceptionToErrorEntityMapper
) : MovieDetailsRepository, CoroutineScope by CoroutineScope(
    Dispatchers.IO +
            SupervisorJob()
) {
    override suspend fun getMovieDetails(movieId: Int): Flow<Resource<MovieDetails>> {
        return flow {
            // Сначала получаем описание фильма и съемочную группу
            val response = try {
                remoteDataSource.getMovieDetails(
                    GetMovieDetailsRequest(movieId = movieId)
                )
            } catch (exception: MoviesException) {
                emit(Resource.Error(moviesExceptionToErrorEntity.handleException(exception)))
                return@flow
            }

            val imagesDeferred: Deferred<ImagesResponse?>
            val reviewsDeferred: Deferred<ReviewsResponse?>

            // Сразу загружаем первую страницу картинок и отзывов
            coroutineScope {
                imagesDeferred = getImagesFirstPage(movieId.toString())
                reviewsDeferred = getReviewsFirstPage(movieId.toString())
            }

            val imagesResult = imagesDeferred.await()?.let {
                mapper.mapImagesResponseToImagesResult(it)
            }

            val reviewsResult = reviewsDeferred.await()?.let {
                mapper.mapReviewsResponseToReviewsResult(it)

            }

            // Дополняем наш класс загруженными данными и эмитим
            val movieDetails = mapper.mapDetailsResponseToDomain(response).copy(
                images = imagesResult,
                reviews = reviewsResult
            )

            emit(Resource.Success(movieDetails))
        }
    }

    private fun getImagesFirstPage(movieId: String): Deferred<ImagesResponse?> {
        return async {
            try {
                remoteDataSource.getImages(
                    GetImagesRequest(
                        movieId = movieId,
                        page = 1,
                        limit = 10
                    )
                )
            } catch (e: MoviesException) {
                null
            }
        }
    }

    private fun getReviewsFirstPage(movieId: String): Deferred<ReviewsResponse?> {
        return async {
            try {
                remoteDataSource.getReviews(
                    GetReviewsRequest(
                        movieId = movieId,
                        page = 1,
                        limit = 10
                    )
                )
            } catch (e: MoviesException) {
                null
            }
        }
    }

    override suspend fun getSeasonsInfo(
        movieId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<SeasonsResult>> {
        return flow {

        }
    }

    override suspend fun getImages(
        movieId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<ImagesResult>> {
        return flow {

        }
    }

    override suspend fun getReviews(
        movieId: String,
        page: Int,
        limit: Int
    ): Flow<Resource<ReviewsResult>> {
        return flow {

        }
    }
}
