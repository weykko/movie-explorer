package ru.urfu.movie_explorer.core.domain.model

/**
 * Настройки фильтрации и сортировки списка фильмов.
 */
data class MovieFilters(
    val genre: String? = null,
    val minRating: Double? = null,
    val minVoteCount: Int? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val sortBy: MovieSortOption? = null,
) {

    /** Применены ли вообще какие-либо настройки фильтрации (сортировка не считается). */
    val isEmpty: Boolean
        get() = genre == null &&
            minRating == null &&
            minVoteCount == null &&
            minYear == null &&
            maxYear == null

    companion object {
        val Empty: MovieFilters = MovieFilters()
    }
}
