package ru.urfu.movie_explorer.core.data.local.db

import ru.urfu.movie_explorer.core.domain.model.Movie

private const val LIST_SEPARATOR = "\u0001"

internal fun Movie.toEntity(addedAt: Long): FavoriteMovieEntity = FavoriteMovieEntity(
    id = id,
    title = title,
    originalTitle = originalTitle,
    year = year,
    runtimeMinutes = runtimeMinutes,
    genresJoined = genres.joinToString(LIST_SEPARATOR),
    rating = rating,
    votes = votes,
    plot = plot,
    director = director,
    castJoined = cast.joinToString(LIST_SEPARATOR),
    posterUrl = posterUrl,
    addedAt = addedAt,
)

internal fun FavoriteMovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    originalTitle = originalTitle,
    year = year,
    runtimeMinutes = runtimeMinutes,
    genres = genresJoined.splitOrEmpty(),
    rating = rating,
    votes = votes,
    plot = plot,
    director = director,
    cast = castJoined.splitOrEmpty(),
    posterUrl = posterUrl,
)

private fun String.splitOrEmpty(): List<String> =
    if (isEmpty()) emptyList() else split(LIST_SEPARATOR)
