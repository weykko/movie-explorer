package ru.urfu.movie_explorer.ui.screens.movies

import ru.urfu.movie_explorer.data.model.Movie

/**
 * UI-состояние экрана списка фильмов.
 * Сделано sealed, чтобы экран обязательно обработал каждую ветвь.
 */
sealed interface MoviesUiState {

    data object Loading : MoviesUiState

    data class Content(val movies: List<Movie>) : MoviesUiState

    data class Error(val message: String) : MoviesUiState
}
