package ru.urfu.movie_explorer.data.network.mapper

import ru.urfu.movie_explorer.data.network.dto.TitleDto
import ru.urfu.movie_explorer.domain.model.Movie

/**
 * Маппер сетевого DTO в доменную модель.
 */
internal fun TitleDto.toDomain(): Movie = Movie(
    id = id,
    title = primaryTitle ?: originalTitle ?: id,
    originalTitle = originalTitle,
    year = startYear,
    runtimeMinutes = runtimeSeconds?.let { it / SECONDS_IN_MINUTE },
    genres = genres,
    rating = rating?.aggregateRating,
    votes = rating?.voteCount,
    plot = plot,
    director = directors.firstOrNull()?.displayName,
    cast = stars.mapNotNull { it.displayName },
    posterUrl = primaryImage?.url,
)

private const val SECONDS_IN_MINUTE = 60
