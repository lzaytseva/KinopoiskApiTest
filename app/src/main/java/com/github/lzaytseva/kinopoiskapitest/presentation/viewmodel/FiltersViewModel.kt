package com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.lzaytseva.kinopoiskapitest.domain.api.FiltersInteractor
import com.github.lzaytseva.kinopoiskapitest.domain.model.FilterValue
import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters
import com.github.lzaytseva.kinopoiskapitest.presentation.state.FiltersScreenSideEffect
import com.github.lzaytseva.kinopoiskapitest.presentation.state.FiltersScreenState
import com.github.lzaytseva.kinopoiskapitest.util.BaseViewModel
import com.github.lzaytseva.kinopoiskapitest.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FiltersViewModel @Inject constructor(
    private val interactor: FiltersInteractor
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<FiltersScreenState> =
        MutableStateFlow(FiltersScreenState.Initial)
    val uiState: StateFlow<FiltersScreenState> = _uiState.asStateFlow()

    private val _sideEffects: MutableSharedFlow<FiltersScreenSideEffect> =
        MutableSharedFlow()
    val sideEffects: SharedFlow<FiltersScreenSideEffect> = _sideEffects.asSharedFlow()

    private var currentFilters: Filters = Filters()

    init {
        getCurrentFilters()
    }

    private fun getCurrentFilters() {
        viewModelScope.launch {
            interactor.getFilters().collect { filters ->
                currentFilters = filters
                _uiState.value = FiltersScreenState.CurrentFilters(currentFilters)
            }
        }
    }

    fun onCountryFieldClicked() {
        viewModelScope.launch {
            interactor.getCountries().collect {
                when (it) {
                    is Resource.Error -> _sideEffects.emit(
                        FiltersScreenSideEffect.FailedLoadingValues(
                            it.error
                        )
                    )

                    is Resource.Success -> _uiState.emit(
                        FiltersScreenState.FilterValues(it.data, "Страны")
                    )
                }
            }
        }
    }

    fun onGenresFieldClicked() {
        viewModelScope.launch {
            interactor.getGenres().collect {
                when (it) {
                    is Resource.Error -> _sideEffects.emit(
                        FiltersScreenSideEffect.FailedLoadingValues(
                            it.error
                        )
                    )

                    is Resource.Success -> _uiState.emit(
                        FiltersScreenState.FilterValues(it.data, "Жанры")
                    )
                }
            }
        }
    }

    fun onBackButtonClicked() {
        viewModelScope.launch {
            val currentState = _uiState.value
            when (currentState) {
                is FiltersScreenState.FilterValues -> {
                    getCurrentFilters()
                }

                FiltersScreenState.Initial -> {
                    _sideEffects.emit(FiltersScreenSideEffect.NavigateToSearchScreen)
                }

                is FiltersScreenState.CurrentFilters -> {
                    _sideEffects.emit(FiltersScreenSideEffect.NavigateToSearchScreen)
                }
            }
        }
    }

    fun saveFilterValue(filterValue: FilterValue) {
        when (filterValue.type) {
            FilterValue.FilterType.Country -> {
                updateFilters(
                    currentFilters.copy(country = filterValue.name)
                )
            }

            FilterValue.FilterType.Genre -> {
                updateFilters(
                    currentFilters.copy(genre = filterValue.name)
                )
            }
        }
    }

    fun saveDateRange(dateRange: String?) {
        val newFilters = currentFilters.copy(year = dateRange)
        updateFilters(newFilters)
    }

    private fun updateFilters(newFilters: Filters) {
        currentFilters = newFilters
        interactor.saveFilters(newFilters)
        _uiState.value = FiltersScreenState.CurrentFilters(newFilters)
    }

    fun updateKpRatingRange(range: String) {
        val newFilters = currentFilters.copy(ratingKp = range)
        updateFilters(newFilters)
    }

    fun updateAgeRating(rating: String) {
        val newRating = if (rating == "Неважно") {
            null
        } else {
            rating
        }
        val newFilters = currentFilters.copy(
            ageRating = newRating
        )
        updateFilters(newFilters)
    }

    fun updateType(type: String?) {
        val newFilters = currentFilters.copy(
            ageRating = type
        )
        updateFilters(newFilters)
    }

    fun clearFilters() {
        interactor.clearFilters()
        getCurrentFilters()
    }
}