package ru.urfu.movie_explorer.data.repository

import ru.urfu.movie_explorer.data.model.Movie

/**
 * Абстракция над источником данных о фильмах.
 */
interface MovieRepository {

    /** Возвращает полный список доступных фильмов. */
    suspend fun getMovies(): List<Movie>

    /** Возвращает фильм по его IMDb id или `null`, если фильм не найден. */
    suspend fun getMovieById(id: String): Movie?
}
