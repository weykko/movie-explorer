package ru.urfu.movie_explorer.feature.favorites

import ru.urfu.movie_explorer.core.domain.model.Movie

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<Movie> = emptyList(),
)
