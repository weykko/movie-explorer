package ru.urfu.movie_explorer.feature.filters.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.feature.filters.FiltersViewModel

val filtersModule = module {
    viewModel {
        FiltersViewModel(
            observeMovieFilters = get(),
            updateMovieFilters = get(),
            clearMovieFilters = get(),
            filtersBadgeCache = get(),
        )
    }
}
