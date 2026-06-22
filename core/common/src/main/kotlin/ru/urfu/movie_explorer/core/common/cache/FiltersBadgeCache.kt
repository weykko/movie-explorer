package ru.urfu.movie_explorer.core.common.cache

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Общий in-memory кэш для индикатора (бейджа) «применены ли настройки фильтрации».
 *
 * Шарится между MoviesViewModel (наблюдает за DataStore) и FiltersViewModel
 * (обновляет, когда пользователь применяет/сбрасывает фильтры).
 */
class FiltersBadgeCache {

    private val _hasFilters = MutableStateFlow(false)
    val hasFilters: StateFlow<Boolean> = _hasFilters.asStateFlow()

    fun setHasFilters(value: Boolean) {
        _hasFilters.value = value
    }
}
