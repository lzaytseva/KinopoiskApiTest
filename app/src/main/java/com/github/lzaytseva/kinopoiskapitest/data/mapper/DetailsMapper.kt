package com.github.lzaytseva.kinopoiskapitest.data.mapper

import android.content.Context
import com.github.lzaytseva.kinopoiskapitest.R
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.PersonDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.ReviewDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.SeasonsInfoDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.response.MovieDetailsResponse
import com.github.lzaytseva.kinopoiskapitest.domain.model.Actor
import com.github.lzaytseva.kinopoiskapitest.domain.model.CrewMember
import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.domain.model.Review
import javax.inject.Inject

class DetailsMapper @Inject constructor(private val context: Context) {
    fun mapDetailsResponseToDomain(dto: MovieDetailsResponse): MovieDetails {
        return with(dto) {
            MovieDetails(
                ageRating = ageRating?.let { "$it+" },
                origName = alternativeName,
                genres = genresToString(genres, MAX_GENRES_TO_DISPLAY),
                countries = countriesToString(countries, MAX_COUNTRIES_TO_DISPLAY),
                description = description,
                isSeries = isSeries,
                duration = getDuration(),
                name = name,
                actors = persons?.let { getActors(it) },
                crew = persons?.let { getCast(it) },
                posterUrl = poster?.previewUrl ?: poster?.url,
                rating = rating?.kp,
                seasonsInfo = seasonsInfo?.let { seasonsInfoToString(it) },
                year = getYear()
            )
        }
    }

    private fun MovieDetailsResponse.getYear(): String? {
        return if (isSeries == true) {
            releaseYears?.firstOrNull()?.let {
                releaseYearsToString(it)
            }
        } else {
            year?.toString()
        }
    }

    private fun getActors(people: List<PersonDto>): List<Actor> {
        return people.filter {
            it.profession.equals("актеры") && !it.name.isNullOrBlank()
        }.map {
            Actor(
                name = it.name,
                photo = it.photo,
                role = it.description,
                id = it.id
            )
        }
    }

    private fun getCast(people: List<PersonDto>): List<CrewMember> {
        return people.filterNot {
            it.profession.equals("актеры") || it.name.isNullOrBlank()
        }.map {
            CrewMember(
                name = it.name,
                photo = it.photo,
                profession = it.profession,
                id = it.id
            )
        }
    }

    private fun seasonsInfoToString(info: List<SeasonsInfoDto>): String? {
        if (info.isEmpty()) return null

        var episodesCount = 0
        info.forEach { seasonsInfoDto ->
            seasonsInfoDto.episodesCount?.let {
                episodesCount += it
            }
        }
        val seasonsCount = info.size
        val episodesString = context.resources.getQuantityString(
            R.plurals.total_episodes,
            episodesCount,
            episodesCount
        )
        val seasonsString = context.resources.getQuantityString(
            R.plurals.total_seasons,
            seasonsCount,
            seasonsCount
        )
        return context.getString(R.string.values_with_divider, seasonsString, ", ", episodesString)
    }

    private fun Int.getFormattedLength(): String {
        if (this < MINUTES_IN_HOUR) {
            return context.getString(R.string.minutes_count, this)
        }
        val hours = this / MINUTES_IN_HOUR
        val minutes = this % MINUTES_IN_HOUR

        return if (minutes == 0) {
            context.getString(R.string.hours_count, hours)
        } else {
            context.getString(R.string.hours_minutes, hours, minutes)
        }

    }

    fun mapReviewsToDomain(dto: List<ReviewDto>): List<Review>? {
        return if (dto.isEmpty()) {
            null
        } else {
            dto.map {
                Review(
                    author = it.author,
                    date = it.date.substringBefore('T'),
                    id = it.id,
                    review = it.review,
                    title = it.title,
                    type = it.type
                )
            }
        }
    }

    private fun MovieDetailsResponse.getDuration(): String? {
        return if (isSeries == true) {
            seriesLength?.getFormattedLength()
        } else {
            movieLength?.getFormattedLength()
        }
    }


    companion object {
        private const val MAX_GENRES_TO_DISPLAY = 2
        private const val MAX_COUNTRIES_TO_DISPLAY = 1
        private const val MINUTES_IN_HOUR = 60
    }
}