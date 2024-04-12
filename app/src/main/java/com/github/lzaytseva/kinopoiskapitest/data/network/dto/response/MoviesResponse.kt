package com.github.lzaytseva.kinopoiskapitest.data.network.dto.response

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.MovieDto
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("docs") val movies: List<MovieDto>,
    val page: Int,
    val pages: Int
)