package com.github.lzaytseva.kinopoiskapitest.data.network.dto.request

data class SearchMovieByParamsRequest(
    val year: String? = null,
    val ageRating: String? = null,
    val country: String? = null,
    val page: Int = 1,
    val limit: Int
)