package ru.urfu.movie_explorer.di

import org.koin.dsl.module
import ru.urfu.movie_explorer.core.common.cache.FiltersBadgeCache

/**
 * Глобальные UI-зависимости, не привязанные к одному feature-модулю.
 * Сейчас здесь только разделяемый [FiltersBadgeCache] — между списком фильмов и
 * экраном настроек фильтрации.
 */
val appUiModule = module {
    single { FiltersBadgeCache() }
}
