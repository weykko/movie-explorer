package ru.urfu.movie_explorer.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.domain.model.MovieError
import ru.urfu.movie_explorer.domain.usecase.ObservePopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.RefreshPopularMoviesUseCase

/**
 * ViewModel экрана списка фильмов.
 */
class MoviesViewModel(
    private val observePopularMovies: ObservePopularMoviesUseCase,
    private val refreshPopularMovies: RefreshPopularMoviesUseCase,
) : ViewModel() {

    private val loadingFlow = MutableStateFlow(false)
    private val errorFlow = MutableStateFlow<String?>(null)

    val uiState: StateFlow<MoviesUiState> = combine(
        observePopularMovies(),
        loadingFlow,
        errorFlow,
    ) { movies, isLoading, error ->
        when {
            isLoading && movies.isEmpty() -> MoviesUiState.Loading
            error != null && movies.isEmpty() -> MoviesUiState.Error(error)
            else -> MoviesUiState.Content(movies = movies, isRefreshing = isLoading, error = error)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = MoviesUiState.Loading,
    )

    init {
        refresh()
    }

    fun retry() {
        refresh()
    }

    private fun refresh() {
        if (loadingFlow.value) return
        loadingFlow.value = true
        errorFlow.value = null
        viewModelScope.launch {
            try {
                refreshPopularMovies()
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
