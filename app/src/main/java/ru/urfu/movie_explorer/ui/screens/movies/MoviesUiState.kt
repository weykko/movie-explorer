package ru.urfu.movie_explorer.ui.screens.movies

import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.model.MovieSortOption

sealed interface MoviesUiState {

    data object Loading : MoviesUiState

    data class Content(
        val movies: List<Movie>,
        val isRefreshing: Boolean = false,
        val error: MoviesErrorReason? = null,
        val hasActiveFilters: Boolean = false,
        val sortBy: MovieSortOption? = null,
    ) : MoviesUiState
}
