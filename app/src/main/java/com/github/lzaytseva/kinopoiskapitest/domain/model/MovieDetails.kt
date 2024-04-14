package com.github.lzaytseva.kinopoiskapitest.domain.model

data class MovieDetails(
    val ageRating: String?,
    val origName: String?,
    val countries: String?,
    val description: String?,
    val genres: String?,
    val isSeries: Boolean?,
    val duration: String?,
    val name: String?,
    val actors: List<Actor>?,
    val crew: List<CrewMember>?,
    val images: List<String>? = null,
    val reviews: List<Review>? = null,
    val posterUrl: String?,
    val rating: Double?,
    val seasonsInfo: String?,
    val year: String?
)