package com.github.lzaytseva.kinopoiskapitest.presentation.state

import com.github.lzaytseva.kinopoiskapitest.data.exception.model.ErrorEntity

sealed interface FiltersScreenSideEffect {

    data class FailedLoadingValues(val error: ErrorEntity): FiltersScreenSideEffect

    data object NavigateToSearchScreen: FiltersScreenSideEffect

}