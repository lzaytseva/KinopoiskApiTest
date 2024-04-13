package com.github.lzaytseva.kinopoiskapitest.domain.model

data class FactsResult(
    val facts: List<Fact>,
    val currentPage: Int,
    val pages: Int
)