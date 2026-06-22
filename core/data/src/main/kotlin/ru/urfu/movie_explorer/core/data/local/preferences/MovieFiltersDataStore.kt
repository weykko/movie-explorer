package ru.urfu.movie_explorer.core.data.local.preferences

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
import ru.urfu.movie_explorer.core.domain.model.MovieFilters
import ru.urfu.movie_explorer.core.domain.model.MovieSortOption

/** Тонкая обёртка над DataStore Preferences для [MovieFilters]. */
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
            prefs.putOrRemove(KEY_GENRE, filters.genre)
            prefs.putOrRemove(KEY_MIN_RATING, filters.minRating)
            prefs.putOrRemove(KEY_MIN_VOTE_COUNT, filters.minVoteCount)
            prefs.putOrRemove(KEY_MIN_YEAR, filters.minYear)
            prefs.putOrRemove(KEY_MAX_YEAR, filters.maxYear)
            prefs.putOrRemove(KEY_SORT_BY, filters.sortBy?.name)
        }
    }

    private fun <T : Any> androidx.datastore.preferences.core.MutablePreferences.putOrRemove(
        key: Preferences.Key<T>,
        value: T?,
    ) {
        if (value == null) remove(key) else set(key, value)
    }

    suspend fun updateSort(sortBy: MovieSortOption?) {
        dataStore.edit { prefs ->
            if (sortBy != null) prefs[KEY_SORT_BY] = sortBy.name else prefs.remove(KEY_SORT_BY)
        }
    }

    suspend fun clear() {
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

        val Context.movieFiltersDataStore by preferencesDataStore(name = FILE_NAME)
    }
}
