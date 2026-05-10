package ru.urfu.movie_explorer.ui.screens.search

import ru.urfu.movie_explorer.domain.model.Movie

/**
 * UI-состояние экрана поиска фильмов.
 */
data class SearchUiState(
    val query: String = "",
    val results: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false,
)
