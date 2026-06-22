package ru.urfu.movie_explorer.feature.search

import ru.urfu.movie_explorer.core.domain.model.Movie

data class SearchUiState(
    val query: String = "",
    val results: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasSearched: Boolean = false,
)
