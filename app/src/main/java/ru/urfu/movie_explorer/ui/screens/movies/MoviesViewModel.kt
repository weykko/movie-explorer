package ru.urfu.movie_explorer.ui.screens.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.data.repository.MovieRepository

/**
 * ViewModel экрана списка фильмов.
 * Публикует единый [StateFlow] с [MoviesUiState], который экран
 * собирает lifecycle-aware способом.
 */
class MoviesViewModel(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    init {
        loadMovies()
    }

    fun retry() {
        loadMovies()
    }

    private fun loadMovies() {
        _uiState.value = MoviesUiState.Loading
        viewModelScope.launch {
            _uiState.value = runCatching { repository.getMovies() }
                .fold(
                    onSuccess = { MoviesUiState.Content(it) },
                    onFailure = { MoviesUiState.Error(it.message ?: "Не удалось загрузить фильмы") },
                )
        }
    }
}
