package ru.urfu.movie_explorer.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.urfu.movie_explorer.data.local.db.FavoriteMovieDao
import ru.urfu.movie_explorer.data.local.db.toDomain
import ru.urfu.movie_explorer.data.local.db.toEntity
import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.repository.FavoriteMoviesRepository

/**
 * Реализация репозитория избранного поверх Room.
 *
 * Все CRUD-операции делегируются [FavoriteMovieDao]. Время добавления берётся
 * из системных часов и используется только для упорядочивания списка.
 */
class FavoriteMoviesRepositoryImpl(
    private val dao: FavoriteMovieDao,
) : FavoriteMoviesRepository {

    override fun observeFavorites(): Flow<List<Movie>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeIsFavorite(movieId: String): Flow<Boolean> =
        dao.observeIsFavorite(movieId)

    override suspend fun addToFavorites(movie: Movie) {
        dao.upsert(movie.toEntity(addedAt = System.currentTimeMillis()))
    }

    override suspend fun removeFromFavorites(movieId: String) {
        dao.deleteById(movieId)
    }
}
