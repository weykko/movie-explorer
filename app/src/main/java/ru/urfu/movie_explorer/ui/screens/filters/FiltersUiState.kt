package ru.urfu.movie_explorer.ui.screens.filters

data class FiltersUiState(
    val genre: String? = null,
    val minRating: Double? = null,
    val minVoteCount: Int? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val isSaved: Boolean = false,
) {

    val hasAnyFilter: Boolean
        get() = genre != null ||
            minRating != null ||
            minVoteCount != null ||
            minYear != null ||
            maxYear != null
}
