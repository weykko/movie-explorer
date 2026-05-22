package ru.urfu.movie_explorer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.urfu.movie_explorer.di.appModule

class MovieExplorerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MovieExplorerApp)
            modules(appModule)
        }
    }
}
