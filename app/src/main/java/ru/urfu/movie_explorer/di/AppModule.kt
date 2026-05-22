package ru.urfu.movie_explorer.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.data.local.db.AppDatabase
import ru.urfu.movie_explorer.data.local.preferences.MovieFiltersDataStore
import ru.urfu.movie_explorer.data.network.NetworkFactory
import ru.urfu.movie_explorer.data.repository.FavoriteMoviesRepositoryImpl
import ru.urfu.movie_explorer.data.repository.MovieFiltersRepositoryImpl
import ru.urfu.movie_explorer.data.repository.MovieRepositoryImpl
import ru.urfu.movie_explorer.domain.repository.FavoriteMoviesRepository
import ru.urfu.movie_explorer.domain.repository.MovieFiltersRepository
import ru.urfu.movie_explorer.domain.repository.MovieRepository
import ru.urfu.movie_explorer.domain.usecase.ClearMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.GetMovieDetailsUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveFavoriteMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveIsFavoriteUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.ObservePopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.RefreshPopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.SearchMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.ToggleFavoriteMovieUseCase
import ru.urfu.movie_explorer.domain.usecase.UpdateMovieFiltersUseCase
import ru.urfu.movie_explorer.ui.common.FiltersBadgeCache
import ru.urfu.movie_explorer.ui.screens.details.MovieDetailsViewModel
import ru.urfu.movie_explorer.ui.screens.favorites.FavoritesViewModel
import ru.urfu.movie_explorer.ui.screens.filters.FiltersViewModel
import ru.urfu.movie_explorer.ui.screens.movies.MoviesViewModel
import ru.urfu.movie_explorer.ui.screens.search.SearchViewModel

/**
 * Корневой Koin-модуль приложения.
 */
val appModule = module {

    // --- Networking ---
    single { NetworkFactory.createOkHttpClient(androidContext()) }
    single { NetworkFactory.createRetrofit(get()) }
    single { NetworkFactory.createImdbApi(get()) }

    // --- Local storage ---
    single { AppDatabase.create(androidContext()) }
    single { get<AppDatabase>().favoriteMovieDao() }
    single { MovieFiltersDataStore(androidContext()) }

    // --- Data repositories ---
    single<MovieRepository> { MovieRepositoryImpl(api = get()) }
    single<FavoriteMoviesRepository> { FavoriteMoviesRepositoryImpl(dao = get()) }
    single<MovieFiltersRepository> { MovieFiltersRepositoryImpl(dataStore = get()) }

    // --- Shared in-memory cache (Practice 5, task 3) ---
    single { FiltersBadgeCache() }

    // --- Domain (use cases) ---
    factory { ObservePopularMoviesUseCase(repository = get()) }
    factory { RefreshPopularMoviesUseCase(repository = get()) }
    factory { GetMovieDetailsUseCase(repository = get()) }
    factory { SearchMoviesUseCase(repository = get()) }
    factory { ObserveMovieFiltersUseCase(repository = get()) }
    factory { UpdateMovieFiltersUseCase(repository = get()) }
    factory { ClearMovieFiltersUseCase(repository = get()) }
    factory { ObserveFavoriteMoviesUseCase(repository = get()) }
    factory { ObserveIsFavoriteUseCase(repository = get()) }
    factory { ToggleFavoriteMovieUseCase(repository = get()) }

    // --- Presentation (ViewModels) ---
    viewModel {
        MoviesViewModel(
            observePopularMovies = get(),
            refreshPopularMovies = get(),
            observeMovieFilters = get(),
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
    viewModel { SearchViewModel(searchMovies = get()) }
    viewModel {
        FiltersViewModel(
            observeMovieFilters = get(),
            updateMovieFilters = get(),
            clearMovieFilters = get(),
            filtersBadgeCache = get(),
        )
    }
    viewModel { FavoritesViewModel(observeFavorites = get()) }
}
