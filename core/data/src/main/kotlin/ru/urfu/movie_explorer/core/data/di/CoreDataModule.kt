package ru.urfu.movie_explorer.core.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.urfu.movie_explorer.core.data.local.db.AppDatabase
import ru.urfu.movie_explorer.core.data.local.preferences.MovieFiltersDataStore
import ru.urfu.movie_explorer.core.data.local.preferences.UserProfileDataStore
import ru.urfu.movie_explorer.core.data.network.NetworkFactory
import ru.urfu.movie_explorer.core.data.notifications.MovieTimeReminderSchedulerImpl
import ru.urfu.movie_explorer.core.data.repository.FavoriteMoviesRepositoryImpl
import ru.urfu.movie_explorer.core.data.repository.MovieFiltersRepositoryImpl
import ru.urfu.movie_explorer.core.data.repository.MovieRepositoryImpl
import ru.urfu.movie_explorer.core.data.repository.ProfileRepositoryImpl
import ru.urfu.movie_explorer.core.domain.notifications.MovieTimeReminderScheduler
import ru.urfu.movie_explorer.core.domain.repository.FavoriteMoviesRepository
import ru.urfu.movie_explorer.core.domain.repository.MovieFiltersRepository
import ru.urfu.movie_explorer.core.domain.repository.MovieRepository
import ru.urfu.movie_explorer.core.domain.repository.ProfileRepository
import ru.urfu.movie_explorer.core.domain.usecase.ClearMovieFiltersUseCase
import ru.urfu.movie_explorer.core.domain.usecase.GetMovieDetailsUseCase
import ru.urfu.movie_explorer.core.domain.usecase.ObserveFavoriteMoviesUseCase
import ru.urfu.movie_explorer.core.domain.usecase.ObserveIsFavoriteUseCase
import ru.urfu.movie_explorer.core.domain.usecase.ObserveMovieFiltersUseCase
import ru.urfu.movie_explorer.core.domain.usecase.ObservePopularMoviesUseCase
import ru.urfu.movie_explorer.core.domain.usecase.ObserveUserProfileUseCase
import ru.urfu.movie_explorer.core.domain.usecase.RefreshPopularMoviesUseCase
import ru.urfu.movie_explorer.core.domain.usecase.SearchMoviesUseCase
import ru.urfu.movie_explorer.core.domain.usecase.ToggleFavoriteMovieUseCase
import ru.urfu.movie_explorer.core.domain.usecase.UpdateMovieFiltersUseCase
import ru.urfu.movie_explorer.core.domain.usecase.UpdateMovieSortUseCase
import ru.urfu.movie_explorer.core.domain.usecase.UpdateUserProfileUseCase

/**
 * Корневой Koin-модуль слоя данных. Регистрирует всё, что нужно фичам:
 *  - инфраструктуру (сеть, БД, DataStore, scheduler),
 *  - реализации репозиториев (биндинги к доменным контрактам),
 *  - use case'ы.
 */
val coreDataModule = module {
    // --- Networking ---
    single { NetworkFactory.createOkHttpClient(androidContext()) }
    single { NetworkFactory.createRetrofit(get()) }
    single { NetworkFactory.createImdbApi(get()) }

    // --- Local storage ---
    single { AppDatabase.create(androidContext()) }
    single { get<AppDatabase>().favoriteMovieDao() }
    single { MovieFiltersDataStore(androidContext()) }
    single { UserProfileDataStore(androidContext()) }

    // --- Repositories ---
    single<MovieRepository> { MovieRepositoryImpl(api = get(), favoriteMoviesRepository = get()) }
    single<FavoriteMoviesRepository> { FavoriteMoviesRepositoryImpl(dao = get()) }
    single<MovieFiltersRepository> { MovieFiltersRepositoryImpl(dataStore = get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(dataStore = get()) }
    single<MovieTimeReminderScheduler> { MovieTimeReminderSchedulerImpl(context = androidContext()) }

    // --- Use cases ---
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
}
