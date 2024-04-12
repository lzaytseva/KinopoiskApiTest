package com.github.lzaytseva.kinopoiskapitest.data.network.dto.response

import com.github.lzaytseva.kinopoiskapitest.data.network.dto.ImageDto
import com.google.gson.annotations.SerializedName

data class ImagesResponse(
    @SerializedName("docs") val images: List<ImageDto>?,
    val limit: Int,
    val page: Int,
    val pages: Int,
    val total: Int
)