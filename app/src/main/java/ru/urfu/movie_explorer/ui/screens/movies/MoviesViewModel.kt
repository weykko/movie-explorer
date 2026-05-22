package ru.urfu.movie_explorer.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.domain.model.MovieError
import ru.urfu.movie_explorer.domain.model.MovieFilters
import ru.urfu.movie_explorer.domain.model.MovieSortOption
import ru.urfu.movie_explorer.domain.usecase.ObserveMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.ObservePopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.RefreshPopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.UpdateMovieSortUseCase
import ru.urfu.movie_explorer.ui.common.FiltersBadgeCache

/**
 * ViewModel экрана списка фильмов.
 */
class MoviesViewModel(
    observePopularMovies: ObservePopularMoviesUseCase,
    private val refreshPopularMovies: RefreshPopularMoviesUseCase,
    observeMovieFilters: ObserveMovieFiltersUseCase,
    private val updateMovieSort: UpdateMovieSortUseCase,
    filtersBadgeCache: FiltersBadgeCache,
) : ViewModel() {

    private val loadingFlow = MutableStateFlow(false)
    private val errorFlow = MutableStateFlow<MoviesErrorReason?>(null)
    private val filtersFlow: StateFlow<MovieFilters> = observeMovieFilters()
        .onEach { filtersBadgeCache.setHasFilters(!it.isEmpty) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MovieFilters(),
        )

    private var refreshJob: Job? = null

    val uiState: StateFlow<MoviesUiState> = combine(
        observePopularMovies(),
        filtersFlow,
        loadingFlow,
        errorFlow,
    ) { movies, filters, isLoading, error ->
        if (isLoading && movies.isEmpty() && error == null) {
            MoviesUiState.Loading
        } else {
            MoviesUiState.Content(
                movies = movies,
                isRefreshing = isLoading,
                error = error,
                hasActiveFilters = !filters.isEmpty,
                sortBy = filters.sortBy,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = MoviesUiState.Loading,
    )

    init {
        filtersFlow
            .onEach { refresh(it) }
            .launchIn(viewModelScope)
    }

    fun retry() {
        refresh(filtersFlow.value)
    }

    fun onSortSelected(sortBy: MovieSortOption?) {
        if (sortBy == filtersFlow.value.sortBy) return
        viewModelScope.launch { updateMovieSort(sortBy) }
    }

    private fun refresh(filters: MovieFilters) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            loadingFlow.value = true
            errorFlow.value = null
            try {
                refreshPopularMovies(filters = filters)
            } catch (e: MovieError) {
                errorFlow.value = e.toReason()
            } finally {
                loadingFlow.value = false
            }
        }
    }

    private fun MovieError.toReason(): MoviesErrorReason = when (this) {
        is MovieError.Server -> if (code != null && code >= SERVER_ERROR_FROM) {
            MoviesErrorReason.ServerUnavailable
        } else {
            MoviesErrorReason.RequestFailed
        }
        is MovieError.Network -> MoviesErrorReason.Network
        is MovieError.NotFound -> MoviesErrorReason.RequestFailed
        is MovieError.Unknown -> MoviesErrorReason.Network
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val SERVER_ERROR_FROM = 500
    }
}
