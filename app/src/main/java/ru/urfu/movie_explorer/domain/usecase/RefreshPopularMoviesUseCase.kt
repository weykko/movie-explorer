package ru.urfu.movie_explorer.domain.usecase

import ru.urfu.movie_explorer.domain.model.MovieFilters
import ru.urfu.movie_explorer.domain.repository.MovieRepository

/**
 * UseCase: обновить кэш популярных фильмов запросом к сети с учётом активных фильтров.
 */
class RefreshPopularMoviesUseCase(
    private val repository: MovieRepository,
) {

    suspend operator fun invoke(
        filters: MovieFilters = MovieFilters(),
        limit: Int = DEFAULT_LIMIT,
    ) {
        repository.refreshPopularMovies(filters = filters, limit = limit)
    }

    private companion object {
        const val DEFAULT_LIMIT = 25
    }
}
