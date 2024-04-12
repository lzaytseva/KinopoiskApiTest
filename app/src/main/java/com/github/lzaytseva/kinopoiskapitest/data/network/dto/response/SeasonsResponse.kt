package com.github.lzaytseva.kinopoiskapitest.data.network.dto.response

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.SeasonDto
import com.google.gson.annotations.SerializedName

data class SeasonsResponse(
    @SerializedName("docs") val seasons: List<SeasonDto>,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)