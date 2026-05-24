package ru.urfu.movie_explorer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.urfu.movie_explorer.di.appModule

/**
 * Application-класс приложения.
 *
 * Точка инициализации Koin. Вызывается до создания [MainActivity], поэтому
 * к моменту первого `koinViewModel()` контейнер уже готов.
 */
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
