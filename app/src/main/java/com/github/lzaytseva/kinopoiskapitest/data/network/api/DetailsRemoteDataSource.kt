package com.github.lzaytseva.kinopoiskapitest.data.network.api

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetImagesRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetMovieDetailsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetReviewsRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.GetSeasonsInfoRequest
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ImagesResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MovieDetailsResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.ReviewsResponse
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.SeasonsResponse

interface DetailsRemoteDataSource {

    suspend fun getMovieDetails(request: GetMovieDetailsRequest): MovieDetailsResponse

    suspend fun getSeasonsInfo(request: GetSeasonsInfoRequest): SeasonsResponse

    suspend fun getImages(request: GetImagesRequest): ImagesResponse

    suspend fun getReviews(request: GetReviewsRequest): ReviewsResponse
}