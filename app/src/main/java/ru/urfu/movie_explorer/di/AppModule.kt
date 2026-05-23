package ru.urfu.movie_explorer.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.urfu.movie_explorer.data.local.db.AppDatabase
import ru.urfu.movie_explorer.data.local.preferences.MovieFiltersDataStore
import ru.urfu.movie_explorer.data.local.preferences.UserProfileDataStore

import ru.urfu.movie_explorer.data.network.NetworkFactory
import ru.urfu.movie_explorer.data.repository.FavoriteMoviesRepositoryImpl
import ru.urfu.movie_explorer.data.repository.MovieFiltersRepositoryImpl
import ru.urfu.movie_explorer.data.repository.MovieRepositoryImpl
import ru.urfu.movie_explorer.data.repository.ProfileRepositoryImpl

import ru.urfu.movie_explorer.domain.repository.FavoriteMoviesRepository
import ru.urfu.movie_explorer.domain.repository.MovieFiltersRepository
import ru.urfu.movie_explorer.domain.repository.MovieRepository
import ru.urfu.movie_explorer.domain.repository.ProfileRepository

import ru.urfu.movie_explorer.domain.usecase.ClearMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.GetMovieDetailsUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveFavoriteMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveIsFavoriteUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.ObservePopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.ObserveUserProfileUseCase

import ru.urfu.movie_explorer.domain.usecase.RefreshPopularMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.SearchMoviesUseCase
import ru.urfu.movie_explorer.domain.usecase.ToggleFavoriteMovieUseCase
import ru.urfu.movie_explorer.domain.usecase.UpdateMovieFiltersUseCase
import ru.urfu.movie_explorer.domain.usecase.UpdateMovieSortUseCase
import ru.urfu.movie_explorer.domain.usecase.UpdateUserProfileUseCase

import ru.urfu.movie_explorer.ui.common.FiltersBadgeCache
import ru.urfu.movie_explorer.ui.screens.details.MovieDetailsViewModel
import ru.urfu.movie_explorer.ui.screens.favorites.FavoritesViewModel
import ru.urfu.movie_explorer.ui.screens.filters.FiltersViewModel
import ru.urfu.movie_explorer.ui.screens.movies.MoviesViewModel
import ru.urfu.movie_explorer.ui.screens.profile.ProfileViewModel
import ru.urfu.movie_explorer.ui.screens.profile.edit.EditProfileViewModel
import ru.urfu.movie_explorer.ui.screens.search.SearchViewModel


/**
 * Корневой Koin-модуль приложения.
 */
val appModule = module {

    single { NetworkFactory.createOkHttpClient(androidContext()) }
    single { NetworkFactory.createRetrofit(get()) }
    single { NetworkFactory.createImdbApi(get()) }

    single { AppDatabase.create(androidContext()) }
    single { get<AppDatabase>().favoriteMovieDao() }
    single { MovieFiltersDataStore(androidContext()) }
    single { UserProfileDataStore(androidContext()) }


    single<MovieRepository> { MovieRepositoryImpl(api = get(), favoriteMoviesRepository = get()) }
    single<FavoriteMoviesRepository> { FavoriteMoviesRepositoryImpl(dao = get()) }
    single<MovieFiltersRepository> { MovieFiltersRepositoryImpl(dataStore = get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(dataStore = get()) }


    single { FiltersBadgeCache() }

    factory { ObservePopularMoviesUseCase(repository = get()) }
    factory { RefreshPopularMoviesUseCase(repository = get()) }
    factory { GetMovieDetailsUseCase(repository = get()) }
    factory { SearchMoviesUseCase(repository = get()) }
    factory { ObserveMovieFiltersUseCase(repository = get()) }
    factory { UpdateMovieFiltersUseCase(repository = get()) }
    factory { UpdateMovieSortUseCase(repository = get()) }
    factory { ClearMovieFiltersUseCase(repository = get()) }
    factory { ObserveFavoriteMoviesUseCase(repository = get()) }
    factory { ObserveIsFavoriteUseCase(repository = get()) }
    factory { ToggleFavoriteMovieUseCase(repository = get()) }
    factory { ObserveUserProfileUseCase(repository = get()) }
    factory { UpdateUserProfileUseCase(repository = get()) }


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
    viewModel { ProfileViewModel(observeUserProfile = get()) }
    viewModel {
        EditProfileViewModel(
            observeUserProfile = get(),
            updateUserProfile = get(),
        )
    }
}
