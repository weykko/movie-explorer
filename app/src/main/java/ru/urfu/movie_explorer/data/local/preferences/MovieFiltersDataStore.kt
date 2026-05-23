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
import ru.urfu.movie_explorer.domain.model.MovieSortOption

/**
 * Тонкая обёртка над DataStore Preferences для [MovieFilters].
 */
class MovieFiltersDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> = context.applicationContext.movieFiltersDataStore

    val filters: Flow<MovieFilters> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            MovieFilters(
                genre = prefs[KEY_GENRE]?.takeIf { it.isNotBlank() },
                minRating = prefs[KEY_MIN_RATING],
                minVoteCount = prefs[KEY_MIN_VOTE_COUNT],
                minYear = prefs[KEY_MIN_YEAR],
                maxYear = prefs[KEY_MAX_YEAR],
                sortBy = prefs[KEY_SORT_BY]
                    ?.let { name -> runCatching { MovieSortOption.valueOf(name) }.getOrNull() },
            )
        }

    suspend fun update(filters: MovieFilters) {
        dataStore.edit { prefs ->
            if (filters.genre != null) prefs[KEY_GENRE] = filters.genre else prefs.remove(KEY_GENRE)
            if (filters.minRating != null) prefs[KEY_MIN_RATING] = filters.minRating else prefs.remove(KEY_MIN_RATING)
            if (filters.minVoteCount != null) prefs[KEY_MIN_VOTE_COUNT] = filters.minVoteCount else prefs.remove(KEY_MIN_VOTE_COUNT)
            if (filters.minYear != null) prefs[KEY_MIN_YEAR] = filters.minYear else prefs.remove(KEY_MIN_YEAR)
            if (filters.maxYear != null) prefs[KEY_MAX_YEAR] = filters.maxYear else prefs.remove(KEY_MAX_YEAR)
            if (filters.sortBy != null) prefs[KEY_SORT_BY] = filters.sortBy.name else prefs.remove(KEY_SORT_BY)
        }
    }

    suspend fun updateSort(sortBy: MovieSortOption?) {
        dataStore.edit { prefs ->
            if (sortBy != null) prefs[KEY_SORT_BY] = sortBy.name else prefs.remove(KEY_SORT_BY)
        }
    }

    suspend fun clear() {
        // Сортировка считается отдельным «вкусом» пользователя — не сбрасываем её при reset фильтров.
        dataStore.edit { prefs ->
            prefs.remove(KEY_GENRE)
            prefs.remove(KEY_MIN_RATING)
            prefs.remove(KEY_MIN_VOTE_COUNT)
            prefs.remove(KEY_MIN_YEAR)
            prefs.remove(KEY_MAX_YEAR)
        }
    }

    private companion object {
        const val FILE_NAME = "movie_filters"
        val KEY_GENRE = stringPreferencesKey("genre")
        val KEY_MIN_RATING = doublePreferencesKey("min_rating")
        val KEY_MIN_VOTE_COUNT = intPreferencesKey("min_vote_count")
        val KEY_MIN_YEAR = intPreferencesKey("min_year")
        val KEY_MAX_YEAR = intPreferencesKey("max_year")
        val KEY_SORT_BY = stringPreferencesKey("sort_by")

        // Делегат верхнего уровня: один DataStore-инстанс на приложение.
        val Context.movieFiltersDataStore by preferencesDataStore(name = FILE_NAME)
    }
}
