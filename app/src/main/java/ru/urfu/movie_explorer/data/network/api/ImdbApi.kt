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

    /** Лента «свежих/популярных» фильмов. */
    @GET("titles")
    suspend fun getPopularTitles(
        @Query("limit") limit: Int = DEFAULT_LIMIT,
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
