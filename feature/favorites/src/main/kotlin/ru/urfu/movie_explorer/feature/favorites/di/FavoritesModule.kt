package ru.urfu.movie_explorer.feature.favorites.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.feature.favorites.FavoritesViewModel

val favoritesModule = module {
    viewModel { FavoritesViewModel(observeFavorites = get()) }
}
