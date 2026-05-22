package ru.urfu.movie_explorer.ui.screens.favorites

import ru.urfu.movie_explorer.domain.model.Movie

data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favorites: List<Movie> = emptyList(),
)
