package ru.urfu.movie_explorer.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.core.domain.model.Movie

/**
 * Контракт хранилища «избранного».
 */
interface FavoriteMoviesRepository {

    /** Поток списка всех избранных фильмов в порядке добавления (новые сверху). */
    fun observeFavorites(): Flow<List<Movie>>

    /** Поток флага «фильм с этим id в избранном». */
    fun observeIsFavorite(movieId: String): Flow<Boolean>

    /**
     * Одноразовое чтение избранного фильма из локальной БД. Используется, когда
     * нужны детали без похода в сеть (оффлайн-режим). Возвращает `null`, если фильм не избран.
     */
    suspend fun getFavorite(movieId: String): Movie?

    /** Добавляет фильм в избранное. */
    suspend fun addToFavorites(movie: Movie)

    /** Удаляет фильм из избранного. */
    suspend fun removeFromFavorites(movieId: String)
}
