package ru.urfu.movie_explorer.data.network.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.urfu.movie_explorer.data.network.dto.TitleDto
import ru.urfu.movie_explorer.data.network.dto.TitlesPageDto

/**
 * Описание REST API IMDb (https://api.imdbapi.dev).
 */
interface ImdbApi {

    /**
     * Лента фильмов с серверной фильтрацией и сортировкой.
     *
     * @param genres Список жанров (collectionFormat: multi — несколько `genres=` параметров).
     * @param startYear Минимальный год выпуска (поиск по диапазону `[startYear, ∞)`).
     * @param minAggregateRating Минимальный средний рейтинг IMDb (0.0–10.0).
     * @param sortBy Способ сортировки (см. `SORT_BY_*` в документации `/titles`).
     * @param sortOrder Направление сортировки (`ASC` / `DESC`).
     */
    @GET("titles")
    suspend fun getPopularTitles(
        @Query("limit") limit: Int = DEFAULT_LIMIT,
        @Query("genres") genres: List<String>? = null,
        @Query("startYear") startYear: Int? = null,
        @Query("endYear") endYear: Int? = null,
        @Query("minAggregateRating") minAggregateRating: Double? = null,
        @Query("minVoteCount") minVoteCount: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null,
    ): TitlesPageDto

    /** Полная информация о фильме по его IMDb id. */
    @GET("titles/{titleId}")
    suspend fun getTitle(
        @Path("titleId") titleId: String,
    ): TitleDto

    /** Поиск по строке запроса. */
    @GET("search/titles")
    suspend fun searchTitles(
        @Query("query") query: String,
        @Query("limit") limit: Int = DEFAULT_LIMIT,
    ): TitlesPageDto

    companion object {
        const val BASE_URL = "https://api.imdbapi.dev/"
        const val DEFAULT_LIMIT = 25
    }
}
