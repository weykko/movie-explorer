package ru.urfu.movie_explorer.domain.usecase

import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.repository.MovieRepository

/**
 * UseCase: получить детали фильма по его IMDb id.
 */
class GetMovieDetailsUseCase(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(id: String): Movie = repository.getMovieById(id)
}
