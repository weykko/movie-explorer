package ru.urfu.movie_explorer.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO ответа IMDb API для одного title.
 */
@Serializable
data class TitleDto(
    @SerialName("id") val id: String,
    @SerialName("primaryTitle") val primaryTitle: String? = null,
    @SerialName("originalTitle") val originalTitle: String? = null,
    @SerialName("primaryImage") val primaryImage: ImageDto? = null,
    @SerialName("startYear") val startYear: Int? = null,
    @SerialName("runtimeSeconds") val runtimeSeconds: Int? = null,
    @SerialName("genres") val genres: List<String> = emptyList(),
    @SerialName("rating") val rating: RatingDto? = null,
    @SerialName("plot") val plot: String? = null,
    @SerialName("directors") val directors: List<NamedPersonDto> = emptyList(),
    @SerialName("stars") val stars: List<NamedPersonDto> = emptyList(),
)

@Serializable
data class ImageDto(
    @SerialName("url") val url: String? = null,
    @SerialName("width") val width: Int? = null,
    @SerialName("height") val height: Int? = null,
)

@Serializable
data class RatingDto(
    @SerialName("aggregateRating") val aggregateRating: Double? = null,
    @SerialName("voteCount") val voteCount: Int? = null,
)

@Serializable
data class NamedPersonDto(
    @SerialName("id") val id: String,
    @SerialName("displayName") val displayName: String? = null,
)
