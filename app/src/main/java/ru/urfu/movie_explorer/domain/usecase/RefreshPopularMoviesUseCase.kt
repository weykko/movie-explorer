package ru.urfu.movie_explorer.domain.usecase

import ru.urfu.movie_explorer.domain.repository.MovieRepository

/**
 * UseCase: обновить кэш популярных фильмов запросом к сети.
 */
class RefreshPopularMoviesUseCase(
    private val repository: MovieRepository,
) {

    suspend operator fun invoke(limit: Int = DEFAULT_LIMIT) {
        repository.refreshPopularMovies(limit)
    }

    private companion object {
        const val DEFAULT_LIMIT = 25
    }
}
