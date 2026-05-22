package ru.urfu.movie_explorer.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.domain.model.Movie

/**
 * Контракт хранилища «избранного».
 *
 * Реализация (data-слой) использует Room, что даёт офлайн-доступ к избранным
 * фильмам без обращения к сети.
 */
interface FavoriteMoviesRepository {

    /** Поток списка всех избранных фильмов в порядке добавления (новые сверху). */
    fun observeFavorites(): Flow<List<Movie>>

    /** Поток флага «фильм с этим id в избранном». */
    fun observeIsFavorite(movieId: String): Flow<Boolean>

    /** Добавляет фильм в избранное. */
    suspend fun addToFavorites(movie: Movie)

    /** Удаляет фильм из избранного. */
    suspend fun removeFromFavorites(movieId: String)
}
