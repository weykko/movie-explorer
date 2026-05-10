package ru.urfu.movie_explorer.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.data.repository.MockMovieRepository
import ru.urfu.movie_explorer.data.repository.MovieRepository
import ru.urfu.movie_explorer.ui.screens.details.MovieDetailsViewModel
import ru.urfu.movie_explorer.ui.screens.movies.MoviesViewModel

/**
 * Корневой Koin-модуль приложения.
 */
val appModule = module {

    // Моки данных
    single<MovieRepository> { MockMovieRepository() }

    viewModel { MoviesViewModel(repository = get()) }
    viewModel { (movieId: String) ->
        MovieDetailsViewModel(repository = get(), movieId = movieId)
    }
}
