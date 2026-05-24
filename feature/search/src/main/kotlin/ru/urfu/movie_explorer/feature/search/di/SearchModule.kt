package ru.urfu.movie_explorer.feature.search.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.feature.search.SearchViewModel

val searchModule = module {
    viewModel { SearchViewModel(searchMovies = get()) }
}
