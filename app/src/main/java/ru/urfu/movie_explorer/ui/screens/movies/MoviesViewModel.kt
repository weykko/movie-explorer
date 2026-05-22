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
import ru.urfu.movie_explorer.domain.usecase.ObserveMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.ObservePopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.RefreshPopularMoviesUseCase
import ru.urfu.movie_explorer.ui.common.FiltersBadgeCache

/**
 * ViewModel экрана списка фильмов.
 *
 * Фильтрация выполняется на сервере: при изменении [MovieFilters] в DataStore автоматически
 * перезагружаем популярные фильмы с новыми query-параметрами IMDb API.
 *
 * Также синхронизирует [FiltersBadgeCache] (бейдж в нижней навигации) при изменении
 * фильтров: пользователь мог поменять их вне этого экрана.
 */
class MoviesViewModel(
    observePopularMovies: ObservePopularMoviesUseCase,
    private val refreshPopularMovies: RefreshPopularMoviesUseCase,
    observeMovieFilters: ObserveMovieFiltersUseCase,
    filtersBadgeCache: FiltersBadgeCache,
) : ViewModel() {

    private val loadingFlow = MutableStateFlow(false)
    private val errorFlow = MutableStateFlow<String?>(null)
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
        when {
            isLoading && movies.isEmpty() -> MoviesUiState.Loading
            error != null && movies.isEmpty() -> MoviesUiState.Error(error)
            else -> MoviesUiState.Content(
                movies = movies,
                isRefreshing = isLoading,
                error = error,
                hasActiveFilters = !filters.isEmpty,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = MoviesUiState.Loading,
    )

    init {
        // Любое изменение фильтров автоматически тянет за собой свежий запрос к API.
        filtersFlow
            .onEach { refresh(it) }
            .launchIn(viewModelScope)
    }

    fun retry() {
        refresh(filtersFlow.value)
    }

    private fun refresh(filters: MovieFilters) {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            loadingFlow.value = true
            errorFlow.value = null
            try {
                refreshPopularMovies(filters = filters)
            } catch (e: MovieError) {
                errorFlow.value = e.message
            } finally {
                loadingFlow.value = false
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
