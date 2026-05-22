package ru.urfu.movie_explorer.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.domain.model.MovieError
import ru.urfu.movie_explorer.domain.usecase.GetMovieDetailsUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveIsFavoriteUseCase
import ru.urfu.movie_explorer.domain.usecase.ToggleFavoriteMovieUseCase

/**
 * ViewModel экрана деталей фильма.
 */
class MovieDetailsViewModel(
    private val getMovieDetails: GetMovieDetailsUseCase,
    observeIsFavorite: ObserveIsFavoriteUseCase,
    private val toggleFavorite: ToggleFavoriteMovieUseCase,
    private val movieId: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = observeIsFavorite(movieId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = false,
    )

    init {
        loadMovie()
    }

    fun retry() {
        loadMovie()
    }

    fun onFavoriteToggleClicked() {
        val movie = (_uiState.value as? MovieDetailsUiState.Content)?.movie ?: return
        viewModelScope.launch {
            toggleFavorite(movie = movie, isCurrentlyFavorite = isFavorite.value)
        }
    }

    private fun loadMovie() {
        _uiState.value = MovieDetailsUiState.Loading
        viewModelScope.launch {
            _uiState.value = try {
                MovieDetailsUiState.Content(getMovieDetails(movieId))
            } catch (e: MovieError) {
                MovieDetailsUiState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
