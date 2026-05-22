package ru.urfu.movie_explorer.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.domain.model.Movie
import ru.urfu.movie_explorer.domain.repository.FavoriteMoviesRepository

class ObserveFavoriteMoviesUseCase(
    private val repository: FavoriteMoviesRepository,
) {
    operator fun invoke(): Flow<List<Movie>> = repository.observeFavorites()
}

class ObserveIsFavoriteUseCase(
    private val repository: FavoriteMoviesRepository,
) {
    operator fun invoke(movieId: String): Flow<Boolean> = repository.observeIsFavorite(movieId)
}

class ToggleFavoriteMovieUseCase(
    private val repository: FavoriteMoviesRepository,
) {

    suspend operator fun invoke(movie: Movie, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            repository.removeFromFavorites(movie.id)
        } else {
            repository.addToFavorites(movie)
        }
    }
}
