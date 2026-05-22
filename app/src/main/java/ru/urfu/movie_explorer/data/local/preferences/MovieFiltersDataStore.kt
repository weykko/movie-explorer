package ru.urfu.movie_explorer.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.urfu.movie_explorer.domain.model.MovieFilters

/**
 * Тонкая обёртка над DataStore Preferences для [MovieFilters].
 *
 * Используется реализацией репозитория, чтобы вся работа с ключами Preferences
 * была сосредоточена в одном месте.
 */
class MovieFiltersDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> = context.applicationContext.movieFiltersDataStore

    val filters: Flow<MovieFilters> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            MovieFilters(
                genre = prefs[KEY_GENRE]?.takeIf { it.isNotBlank() },
                minRating = prefs[KEY_MIN_RATING],
                minYear = prefs[KEY_MIN_YEAR],
            )
        }

    suspend fun update(filters: MovieFilters) {
        dataStore.edit { prefs ->
            if (filters.genre != null) prefs[KEY_GENRE] = filters.genre else prefs.remove(KEY_GENRE)
            if (filters.minRating != null) prefs[KEY_MIN_RATING] = filters.minRating else prefs.remove(KEY_MIN_RATING)
            if (filters.minYear != null) prefs[KEY_MIN_YEAR] = filters.minYear else prefs.remove(KEY_MIN_YEAR)
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    private companion object {
        const val FILE_NAME = "movie_filters"
        val KEY_GENRE = stringPreferencesKey("genre")
        val KEY_MIN_RATING = doublePreferencesKey("min_rating")
        val KEY_MIN_YEAR = intPreferencesKey("min_year")

        // Делегат верхнего уровня: один DataStore-инстанс на приложение.
        val Context.movieFiltersDataStore by preferencesDataStore(name = FILE_NAME)
    }
}
