package com.github.lzaytseva.kinopoiskapitest.data.network.dto

data class ReviewDto(
    val author: String,
    val date: String,
    val id: Int,
    val review: String,
    val title: String,
    val type: String
)