package ru.urfu.movie_explorer.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.domain.model.MovieError
import ru.urfu.movie_explorer.domain.usecase.GetMovieDetailsUseCase

/**
 * ViewModel экрана деталей фильма.
 */
class MovieDetailsViewModel(
    private val getMovieDetails: GetMovieDetailsUseCase,
    private val movieId: String,
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    init {
        loadMovie()
    }

    fun retry() {
        loadMovie()
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
}
