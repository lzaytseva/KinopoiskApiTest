package com.github.lzaytseva.kinopoiskapitest.data.network.dto.response

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.ReviewDto
import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    @SerializedName("docs") val reviews: List<ReviewDto>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)