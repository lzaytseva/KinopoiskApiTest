package com.github.lzaytseva.kinopoiskapitest.domain.model

data class ImagesResult(
    val imagesUrls: List<String>,
    val currentPage: Int,
    val pages: Int
)