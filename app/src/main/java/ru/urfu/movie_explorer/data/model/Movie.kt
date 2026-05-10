package ru.urfu.movie_explorer.data.model

/**
 * Доменная модель фильма.
 */
data class Movie(
    val id: String,
    val title: String,
    val originalTitle: String?,
    val year: Int,
    val runtimeMinutes: Int?,
    val genres: List<String>,
    val rating: Double?,
    val votes: Int?,
    val plot: String,
    val director: String?,
    val cast: List<String>,
    val posterUrl: String?,
)
