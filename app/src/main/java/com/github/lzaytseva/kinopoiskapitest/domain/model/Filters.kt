package com.github.lzaytseva.kinopoiskapitest.domain.model

data class Filters(
    val country: String? = null,
    val ageRating: String? = null,
    val year: String? = null,
    val genre: String? = null,
    val type: String? = null,
    val ratingKp: String? = null
)
