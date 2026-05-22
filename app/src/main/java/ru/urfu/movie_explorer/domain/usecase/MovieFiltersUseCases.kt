package ru.urfu.movie_explorer.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.domain.model.MovieFilters
import ru.urfu.movie_explorer.domain.model.MovieSortOption
import ru.urfu.movie_explorer.domain.repository.MovieFiltersRepository

class ObserveMovieFiltersUseCase(
    private val repository: MovieFiltersRepository,
) {
    operator fun invoke(): Flow<MovieFilters> = repository.observe()
}

class UpdateMovieFiltersUseCase(
    private val repository: MovieFiltersRepository,
) {
    suspend operator fun invoke(filters: MovieFilters) {
        repository.update(filters)
    }
}

class ClearMovieFiltersUseCase(
    private val repository: MovieFiltersRepository,
) {
    suspend operator fun invoke() {
        repository.clear()
    }
}

class UpdateMovieSortUseCase(
    private val repository: MovieFiltersRepository,
) {
    suspend operator fun invoke(sortBy: MovieSortOption?) {
        repository.updateSort(sortBy)
    }
}
