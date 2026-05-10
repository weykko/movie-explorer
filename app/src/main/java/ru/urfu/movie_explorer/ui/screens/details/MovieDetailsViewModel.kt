package ru.urfu.movie_explorer.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.data.repository.MovieRepository

/**
 * ViewModel экрана деталей фильма.
 */
class MovieDetailsViewModel(
    private val repository: MovieRepository,
    private val movieId: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        loadMovie()
    }

    private fun loadMovie() {
        _uiState.value = MovieDetailsUiState.Loading
        viewModelScope.launch {
            _uiState.value = runCatching { repository.getMovieById(movieId) }
                .fold(
                    onSuccess = { movie ->
                        if (movie != null) MovieDetailsUiState.Content(movie)
                        else MovieDetailsUiState.Error("Фильм не найден")
                    },
                    onFailure = { MovieDetailsUiState.Error(it.message ?: "Ошибка загрузки") },
                )
        }
    }
}
