package com.github.lzaytseva.kinopoiskapitest.data.network.dto

data class EpisodeDto(
    val airDate: String?,
    val description: String?,
    val enName: String?,
    val name: String?,
    val number: Int?,
    val still: ImageDto?
)