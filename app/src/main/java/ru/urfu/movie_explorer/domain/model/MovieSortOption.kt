package ru.urfu.movie_explorer.domain.model

/**
 * Способ сортировки списка фильмов на сервере IMDb API.
 */
enum class MovieSortOption(val apiValue: String, val apiSortOrder: String) {
    ReleaseDate(apiValue = "SORT_BY_RELEASE_DATE", apiSortOrder = "DESC"),
    UserRating(apiValue = "SORT_BY_USER_RATING", apiSortOrder = "DESC"),
    RatingCount(apiValue = "SORT_BY_USER_RATING_COUNT", apiSortOrder = "DESC"),
}
