package com.github.lzaytseva.kinopoiskapitest.presentation.state

import com.github.lzaytseva.kinopoiskapitest.domain.model.FilterValue
import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters

sealed interface FiltersScreenState {

    data object Initial : FiltersScreenState

    data class FilterValues(
        val values: List<FilterValue>,
        val title: String
    ) : FiltersScreenState

    data class CurrentFilters(
        val filters: Filters
    ): FiltersScreenState
}