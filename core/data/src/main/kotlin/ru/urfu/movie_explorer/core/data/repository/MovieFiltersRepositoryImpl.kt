package ru.urfu.movie_explorer.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.core.data.local.preferences.MovieFiltersDataStore
import ru.urfu.movie_explorer.core.domain.model.MovieFilters
import ru.urfu.movie_explorer.core.domain.model.MovieSortOption
import ru.urfu.movie_explorer.core.domain.repository.MovieFiltersRepository

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
