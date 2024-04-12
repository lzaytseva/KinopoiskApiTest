package com.github.lzaytseva.kinopoiskapitest.data.network.dto

data class SeasonDto(
    val episodes: List<EpisodeDto>?,
    val episodesCount: Int?,
    val number: Int?,
)