package ru.urfu.movie_explorer.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.repository.MovieRepository

class ObservePopularMoviesUseCase(
    private val repository: MovieRepository,
) {
    operator fun invoke(): Flow<List<Movie>> = repository.observePopularMovies()
}
