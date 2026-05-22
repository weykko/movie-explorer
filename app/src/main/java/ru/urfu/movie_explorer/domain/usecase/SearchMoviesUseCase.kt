package ru.urfu.movie_explorer.domain.usecase

import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.repository.MovieRepository

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
