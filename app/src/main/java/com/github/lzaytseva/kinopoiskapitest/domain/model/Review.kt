package com.github.lzaytseva.kinopoiskapitest.domain.model

data class Review(
    val author: String,
    val date: String,
    val id: Int,
    val review: String,
    val title: String,
    val type: String
)