package com.github.lzaytseva.kinopoiskapitest.data.mapper

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.CountryDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.GenreDto
import com.github.lzaytseva.kinopoiskapitest.data.network.dto.ReleaseYearDto

fun genresToString(genres: List<GenreDto>?, takeCount: Int): String? {
    return genres
        ?.take(takeCount)
        ?.map { it.name }
        ?.joinToString()
}

fun countriesToString(countries: List<CountryDto>?, takeCount: Int): String? {
    return countries
        ?.take(takeCount)
        ?.map { it.name }
        ?.joinToString()
}

fun releaseYearsToString(yearDto: ReleaseYearDto): String? {
    val start = yearDto.start
    val end = yearDto.end
    return when {
        start != null && end != null && start != end -> "$start-$end"
        start != null && end != null && start == end -> start.toString()
        start != null -> "с $start"
        else -> null
    }
}
