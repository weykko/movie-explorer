package ru.urfu.movie_explorer.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.model.MovieFilters

/**
 * Контракт репозитория фильмов в доменном слое.
 */
interface MovieRepository {

    /**
     * Поток с последним загруженным списком популярных фильмов.
     * Используется для in-memory кэширования между навигациями (бонус практики 4).
     */
    fun observePopularMovies(): Flow<List<Movie>>

    /**
     * Загружает фильмы с серверной фильтрацией ([filters]) и обновляет [observePopularMovies].
     * Может выбросить [ru.urfu.movie_explorer.domain.model.MovieError].
     */
    suspend fun refreshPopularMovies(filters: MovieFilters, limit: Int)

    /**
     * Загружает фильм по его идентификатору.
     * Сначала пытается найти его в кэше популярных фильмов, а если не находит — запрашивает у API.
     */
    suspend fun getMovieById(id: String): Movie

    /**
     * Ищет фильмы по текстовому запросу.
     * Может выбросить [ru.urfu.movie_explorer.domain.model.MovieError].
     */
    suspend fun searchMovies(query: String, limit: Int): List<Movie>
}
