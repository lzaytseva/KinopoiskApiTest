package com.github.lzaytseva.kinopoiskapitest.domain.model

data class MovieDetails(
    val ageRating: String?,
    val origName: String?,
    val countries: String?,
    val description: String?,
    val facts: FactsResult? = null,
    val genres: String?,
    val isSeries: Boolean?,
    val movieLength: String?,
    val name: String?,
    val actors: List<Person>?,
    val cast: List<Person>?,
    val images: ImagesResult? = null,
    val reviews: ReviewsResult? = null,
    val posterUrl: String?,
    val rating: Double?,
    val seasonsInfo: String?,
    val seriesLength: String?,
    val year: String?
)