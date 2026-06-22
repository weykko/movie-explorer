package ru.urfu.movie_explorer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import ru.urfu.movie_explorer.core.data.di.coreDataModule
import ru.urfu.movie_explorer.di.appUiModule
import ru.urfu.movie_explorer.feature.favorites.di.favoritesModule
import ru.urfu.movie_explorer.feature.filters.di.filtersModule
import ru.urfu.movie_explorer.feature.movies.di.moviesModule
import ru.urfu.movie_explorer.feature.profile.di.profileModule
import ru.urfu.movie_explorer.feature.search.di.searchModule
import org.koin.core.context.startKoin

class MovieExplorerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MovieExplorerApplication)
            modules(
                coreDataModule,
                appUiModule,
                moviesModule,
                searchModule,
                favoritesModule,
                filtersModule,
                profileModule,
            )
        }
    }
}
