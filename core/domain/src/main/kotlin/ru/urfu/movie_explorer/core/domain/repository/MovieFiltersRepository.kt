package ru.urfu.movie_explorer.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.core.domain.model.MovieFilters
import ru.urfu.movie_explorer.core.domain.model.MovieSortOption

/**
 * Контракт хранилища настроек фильтрации.
 */
interface MovieFiltersRepository {

    /** Реактивный поток текущих фильтров. */
    fun observe(): Flow<MovieFilters>

    /** Полностью заменяет сохранённые фильтры. */
    suspend fun update(filters: MovieFilters)

    /** Меняет только сортировку, не трогая остальные поля. `null` сбрасывает её. */
    suspend fun updateSort(sortBy: MovieSortOption?)

    /** Сбрасывает фильтры в [MovieFilters.Empty]; сортировка не сбрасывается. */
    suspend fun clear()
}
