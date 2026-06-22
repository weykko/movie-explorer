package ru.urfu.movie_explorer.core.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Ответ списковых эндпоинтов IMDb API: `/titles`, `/search/titles`. */
@Serializable
data class TitlesPageDto(
    @SerialName("titles") val titles: List<TitleDto> = emptyList(),
)
