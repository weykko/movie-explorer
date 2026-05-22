package ru.urfu.movie_explorer.ui.common

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Общий in-memory кэш для индикатора (бейджа) «применены ли настройки фильтрации».
 *
 * Согласно требованию практики 5 / задачи 3 — это легковесный класс, общий для экрана
 * списка и экрана настроек, не пишет ничего на диск. Зависимость регистрируется в DI
 * (см. [ru.urfu.movie_explorer.di.appModule]).
 *
 * Экран настроек обновляет значение через [setHasFilters] при применении изменений,
 * экран списка читает его через [hasFilters] для отрисовки бейджа.
 */
class FiltersBadgeCache {

    private val _hasFilters = MutableStateFlow(false)
    val hasFilters: StateFlow<Boolean> = _hasFilters.asStateFlow()

    fun setHasFilters(value: Boolean) {
        _hasFilters.value = value
    }
}
