package ru.urfu.movie_explorer.feature.movies.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.feature.movies.details.MovieDetailsViewModel
import ru.urfu.movie_explorer.feature.movies.list.MoviesViewModel

val moviesModule = module {
    viewModel {
        MoviesViewModel(
            observePopularMovies = get(),
            refreshPopularMovies = get(),
            observeMovieFilters = get(),
            updateMovieSort = get(),
            filtersBadgeCache = get(),
        )
    }
    viewModel { (movieId: String) ->
        MovieDetailsViewModel(
            getMovieDetails = get(),
            observeIsFavorite = get(),
            toggleFavorite = get(),
            movieId = movieId,
        )
    }
}
