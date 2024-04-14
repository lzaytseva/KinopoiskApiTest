package com.github.lzaytseva.kinopoiskapitest.data.network.dto

data class MovieDto(
    val alternativeName: String?,
    val isSeries: Boolean?,
    val countries: List<CountryDto>?,
    val genres: List<GenreDto>?,
    val id: Int,
    val name: String?,
    val poster: ImageDto?,
    val rating: RatingDto?,
    val releaseYears: List<ReleaseYearDto>?,
    val year: Int?
)