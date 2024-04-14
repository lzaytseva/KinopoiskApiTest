package com.github.lzaytseva.kinopoiskapitest.domain.model

data class FilterValue(
    val name: String,
    val type: FilterType,
) {
    sealed interface FilterType {

        data object Country : FilterType

        data object Genre : FilterType
    }
}