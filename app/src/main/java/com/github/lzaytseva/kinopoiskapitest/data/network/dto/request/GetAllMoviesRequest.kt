package com.github.lzaytseva.kinopoiskapitest.data.network.dto.request

data class GetAllMoviesRequest(
    val page: Int = 1,
    val limit: Int
)