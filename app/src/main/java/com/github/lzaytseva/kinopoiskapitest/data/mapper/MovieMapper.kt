package com.github.lzaytseva.kinopoiskapitest.data.mapper

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.MovieDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.SearchMoviesByParamsResponse
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
                posterUrl = poster?.previewUrl,
                ratingKp = rating?.kp,
                year = year,
                genres = genres
                    ?.take(MAX_GENRES_TO_DISPLAY)
                    ?.map { it.name }
                    ?.joinToString(),
                countries = countries
                    ?.take(MAX_COUNTRIES_TO_DISPLAY)
                    ?.map { it.name }
                    ?.joinToString()
            )
        }
    }

    fun searchMovieResponseToMoviesResult(dto: SearchMoviesByParamsResponse): MoviesSearchResult {
        return with(dto) {
            MoviesSearchResult(
                pages = pages,
                movies = movies.map {
                    mapMovieToDomain(it)
                }
            )
        }
    }

    companion object {
        private const val MAX_GENRES_TO_DISPLAY = 2
        private const val MAX_COUNTRIES_TO_DISPLAY = 2
    }
}