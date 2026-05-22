package ru.urfu.movie_explorer.domain.model

/**
 * Настройки фильтрации списка фильмов.
 *
 * Все поля опциональны: `null` означает «фильтр не применяется».
 * Сохраняются в DataStore и загружаются при старте приложения.
 */
data class MovieFilters(
    val genre: String? = null,
    val minRating: Double? = null,
    val minYear: Int? = null,
) {

    /** Применены ли вообще какие-либо настройки. Используется для бейджа в UI. */
    val isEmpty: Boolean
        get() = genre == null && minRating == null && minYear == null

    companion object {
        val Empty: MovieFilters = MovieFilters()
    }
}
