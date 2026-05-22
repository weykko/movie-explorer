package ru.urfu.movie_explorer.ui.screens.movies

import ru.urfu.movie_explorer.domain.model.Movie

sealed interface MoviesUiState {

    data object Loading : MoviesUiState

    data class Content(
        val movies: List<Movie>,
        val isRefreshing: Boolean = false,
        val error: String? = null,
        val hasActiveFilters: Boolean = false,
    ) : MoviesUiState

    data class Error(val message: String) : MoviesUiState
}
