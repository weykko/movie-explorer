package ru.urfu.movie_explorer.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.domain.model.MovieFilters

/**
 * Контракт хранилища настроек фильтрации.
 *
 * Реализация (data-слой) использует DataStore Preferences и обеспечивает,
 * что настройки сохраняются между перезапусками приложения.
 */
interface MovieFiltersRepository {

    /** Реактивный поток текущих фильтров. */
    fun observe(): Flow<MovieFilters>

    /** Полностью заменяет сохранённые фильтры. */
    suspend fun update(filters: MovieFilters)

    /** Сбрасывает фильтры в [MovieFilters.Empty]. */
    suspend fun clear()
}
