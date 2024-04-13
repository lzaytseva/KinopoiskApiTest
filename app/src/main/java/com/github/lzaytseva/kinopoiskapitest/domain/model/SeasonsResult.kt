package com.github.lzaytseva.kinopoiskapitest.domain.model

data class SeasonsResult(
    val seasons: List<Season>,
    val currentPage: Int,
    val pages: Int
)