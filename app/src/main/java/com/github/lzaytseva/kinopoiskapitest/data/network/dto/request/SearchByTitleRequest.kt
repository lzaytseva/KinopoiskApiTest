package com.github.lzaytseva.kinopoiskapitest.data.network.dto.request

data class SearchByTitleRequest(
    val searchQuery: String,
    val page: Int = 1,
    val limit: Int
)