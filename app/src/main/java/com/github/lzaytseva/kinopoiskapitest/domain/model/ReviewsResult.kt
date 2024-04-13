package com.github.lzaytseva.kinopoiskapitest.domain.model

data class ReviewsResult(
    val reviews: List<Review>,
    val currentPage: Int,
    val pages: Int
)