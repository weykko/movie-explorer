package ru.urfu.movie_explorer.feature.movies.list

import ru.urfu.movie_explorer.core.domain.model.Movie
import ru.urfu.movie_explorer.core.domain.model.MovieSortOption

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
