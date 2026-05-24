package ru.urfu.movie_explorer.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.core.domain.model.Movie
import ru.urfu.movie_explorer.core.domain.model.MovieFilters
import ru.urfu.movie_explorer.core.domain.repository.MovieRepository

class ObservePopularMoviesUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(): Flow<List<Movie>> = repository.observePopularMovies()
}

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

class GetMovieDetailsUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(id: String): Movie = repository.getMovieById(id)
}

class SearchMoviesUseCase(
    private val repository: MovieRepository,
) {

    suspend operator fun invoke(query: String, limit: Int = DEFAULT_LIMIT): List<Movie> {
        val trimmed = query.trim()
        if (trimmed.length < MIN_QUERY_LENGTH) return emptyList()
        return repository.searchMovies(trimmed, limit)
    }

    private companion object {
        const val MIN_QUERY_LENGTH = 2
        const val DEFAULT_LIMIT = 20
    }
}
