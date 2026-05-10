package ru.urfu.movie_explorer.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.data.network.NetworkFactory
import ru.urfu.movie_explorer.data.repository.MovieRepositoryImpl
import ru.urfu.movie_explorer.domain.repository.MovieRepository
import ru.urfu.movie_explorer.domain.usecase.GetMovieDetailsUseCase
import ru.urfu.movie_explorer.domain.usecase.ObservePopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.RefreshPopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.SearchMoviesUseCase
import ru.urfu.movie_explorer.ui.screens.details.MovieDetailsViewModel
import ru.urfu.movie_explorer.ui.screens.movies.MoviesViewModel
import ru.urfu.movie_explorer.ui.screens.search.SearchViewModel

/**
 * Корневой Koin-модуль приложения.
 */
val appModule = module {

    single { NetworkFactory.createOkHttpClient(androidContext()) }
    single { NetworkFactory.createRetrofit(get()) }
    single { NetworkFactory.createImdbApi(get()) }

    single<MovieRepository> { MovieRepositoryImpl(api = get()) }

    factory { ObservePopularMoviesUseCase(repository = get()) }
    factory { RefreshPopularMoviesUseCase(repository = get()) }
    factory { GetMovieDetailsUseCase(repository = get()) }
    factory { SearchMoviesUseCase(repository = get()) }

    viewModel {
        MoviesViewModel(
            observePopularMovies = get(),
            refreshPopularMovies = get(),
        )
    }
    viewModel { (movieId: String) ->
        MovieDetailsViewModel(
            getMovieDetails = get(),
            movieId = movieId,
        )
    }
    viewModel { SearchViewModel(searchMovies = get()) }
}
