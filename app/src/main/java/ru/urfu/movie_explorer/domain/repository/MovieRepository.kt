package ru.urfu.movie_explorer.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.domain.model.Movie

/**
 * Контракт репозитория фильмов в доменном слое.
 *
 * Реализация (data-слой) знает про Retrofit/IMDb API. UseCase'ы и UI работают только с этим
 * интерфейсом, что позволяет легко подменить источник данных в тестах или в будущем.
 */
interface MovieRepository {

    /**
     * Поток с последним загруженным списком популярных фильмов.
     * Используется для in-memory кэширования между навигациями (бонус практики 4).
     */
    fun observePopularMovies(): Flow<List<Movie>>

    /**
     * Загружает популярные фильмы и обновляет [observePopularMovies].
     * Может выбросить [ru.urfu.movie_explorer.domain.model.MovieError].
     */
    suspend fun refreshPopularMovies(limit: Int)

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
