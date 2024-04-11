package com.github.lzaytseva.kinopoiskapitest.domain.model

data class Movie(
    val origName: String?,
    val countries: String?,
    val genres: String?,
    val id: Int,
    val ruName: String?,
    val posterUrl: String?,
    val ratingKp: Double?,
    val year: Int?
)
