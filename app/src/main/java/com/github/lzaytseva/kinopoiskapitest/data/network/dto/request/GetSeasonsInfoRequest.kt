package com.github.lzaytseva.kinopoiskapitest.data.network.dto.request

data class GetSeasonsInfoRequest(
    val movieId: String,
    val page: Int,
    val limit: Int
)