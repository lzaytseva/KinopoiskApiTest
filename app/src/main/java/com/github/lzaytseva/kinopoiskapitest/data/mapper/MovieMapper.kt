package com.github.lzaytseva.kinopoiskapitest.data.mapper

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.MovieDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MoviesResponse
import com.github.lzaytseva.kinopoiskapitest.domain.model.Movie
import com.github.lzaytseva.kinopoiskapitest.domain.model.MoviesSearchResult
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    private fun mapMovieToDomain(dto: MovieDto): Movie {
        return with(dto) {
            Movie(
                id = id,
                origName = alternativeName,
                ruName = name,
                posterUrl = poster?.previewUrl ?: poster?.url,
                ratingKp = rating?.kp,
                year = getYear(),
                isSeries = isSeries,
                genres = genresToString(genres, MAX_GENRES_TO_DISPLAY),
                countries = countriesToString(countries, MAX_COUNTRIES_TO_DISPLAY),
            )
        }
    }

    fun searchMovieResponseToMoviesResult(dto: MoviesResponse): MoviesSearchResult {
        return with(dto) {
            MoviesSearchResult(
                currentPage = page,
                pages = pages,
                movies = movies.map {
                    mapMovieToDomain(it)
                }
            )
        }
    }

    private fun MovieDto.getYear(): String? {
        return if (isSeries == true) {
            releaseYears?.let {
                releaseYearsToString(it)
            }
        } else {
            year?.toString()
        }
    }

    companion object {
        private const val MAX_GENRES_TO_DISPLAY = 2
        private const val MAX_COUNTRIES_TO_DISPLAY = 2
    }
}