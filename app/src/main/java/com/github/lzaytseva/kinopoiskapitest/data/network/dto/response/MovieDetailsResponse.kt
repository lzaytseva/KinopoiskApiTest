package com.github.lzaytseva.kinopoiskapitest.data.network.dto.response

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.CountryDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.GenreDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.ImageDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.PersonDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.RatingDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.ReleaseYearDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.SeasonsInfoDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.SimilarMoviesDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.request.FactDto

data class MovieDetailsResponse(
    val ageRating: Int?,
    val alternativeName: String?,
    val countries: List<CountryDto>?,
    val description: String?,
    val facts: List<FactDto>?,
    val genres: List<GenreDto>?,
    val id: Int?,
    val isSeries: Boolean?,
    val movieLength: Int?,
    val name: String?,
    val persons: List<PersonDto>?,
    val poster: ImageDto?,
    val rating: RatingDto?,
    val releaseYears: List<ReleaseYearDto>?,
    val seasonsInfo: List<SeasonsInfoDto>?,
    val seriesLength: Int?,
    val similarMovies: List<SimilarMoviesDto>?,
    val year: Int?
)