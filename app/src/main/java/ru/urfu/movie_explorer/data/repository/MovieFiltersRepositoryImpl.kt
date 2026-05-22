package ru.urfu.movie_explorer.data.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.data.local.preferences.MovieFiltersDataStore
import ru.urfu.movie_explorer.domain.model.MovieFilters
import ru.urfu.movie_explorer.domain.model.MovieSortOption
import ru.urfu.movie_explorer.domain.repository.MovieFiltersRepository

class MovieFiltersRepositoryImpl(
    private val dataStore: MovieFiltersDataStore,
) : MovieFiltersRepository {

    override fun observe(): Flow<MovieFilters> = dataStore.filters

    override suspend fun update(filters: MovieFilters) {
        dataStore.update(filters)
    }

    override suspend fun updateSort(sortBy: MovieSortOption?) {
        dataStore.updateSort(sortBy)
    }

    override suspend fun clear() {
        dataStore.clear()
    }
}
