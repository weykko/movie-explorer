package ru.urfu.movie_explorer.ui.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Общий in-memory кэш для индикатора (бейджа) «применены ли настройки фильтрации».
 */
class FiltersBadgeCache {

    private val _hasFilters = MutableStateFlow(false)
    val hasFilters: StateFlow<Boolean> = _hasFilters.asStateFlow()

    fun setHasFilters(value: Boolean) {
        _hasFilters.value = value
    }
}
