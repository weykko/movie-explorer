package ru.urfu.movie_explorer.ui.screens.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.domain.model.MovieFilters
import ru.urfu.movie_explorer.domain.usecase.ClearMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.UpdateMovieFiltersUseCase
import ru.urfu.movie_explorer.ui.common.FiltersBadgeCache

/**
 * ViewModel экрана настроек фильтрации списка фильмов.
 */
class FiltersViewModel(
    private val observeMovieFilters: ObserveMovieFiltersUseCase,
    private val updateMovieFilters: UpdateMovieFiltersUseCase,
    private val clearMovieFilters: ClearMovieFiltersUseCase,
    private val filtersBadgeCache: FiltersBadgeCache,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = observeMovieFilters().first()
            _uiState.value = FiltersUiState(
                genre = saved.genre,
                minRating = saved.minRating,
                minVoteCount = saved.minVoteCount,
                minYear = saved.minYear,
                maxYear = saved.maxYear,
            )
        }
    }

    fun onGenreChanged(genre: String?) {
        _uiState.value = _uiState.value.copy(genre = genre, isSaved = false)
    }

    fun onMinRatingChanged(value: Double?) {
        _uiState.value = _uiState.value.copy(minRating = value, isSaved = false)
    }

    fun onMinVoteCountChanged(value: Int?) {
        _uiState.value = _uiState.value.copy(minVoteCount = value, isSaved = false)
    }

    fun onMinYearChanged(value: Int?) {
        _uiState.value = _uiState.value.copy(minYear = value, isSaved = false)
    }

    fun onMaxYearChanged(value: Int?) {
        _uiState.value = _uiState.value.copy(maxYear = value, isSaved = false)
    }

    fun apply() {
        val current = _uiState.value
        val filters = MovieFilters(
            genre = current.genre,
            minRating = current.minRating,
            minVoteCount = current.minVoteCount,
            minYear = current.minYear,
            maxYear = current.maxYear,
        )
        viewModelScope.launch {
            updateMovieFilters(filters)
            filtersBadgeCache.setHasFilters(!filters.isEmpty)
            _uiState.value = current.copy(isSaved = true)
        }
    }

    fun reset() {
        viewModelScope.launch {
            clearMovieFilters()
            filtersBadgeCache.setHasFilters(false)
            _uiState.value = FiltersUiState(isSaved = true)
        }
    }
}
