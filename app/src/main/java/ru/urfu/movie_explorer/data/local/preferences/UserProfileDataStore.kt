package ru.urfu.movie_explorer.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ru.urfu.movie_explorer.domain.model.UserProfile

/**
 * Тонкая обёртка над DataStore Preferences для [UserProfile].
 */
class UserProfileDataStore(context: Context) {

    private val dataStore: DataStore<Preferences> = context.applicationContext.userProfileDataStore

    val profile: Flow<UserProfile> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs ->
            UserProfile(
                nickname = prefs[KEY_NICKNAME]?.takeIf { it.isNotBlank() },
                avatarUri = prefs[KEY_AVATAR_URI]?.takeIf { it.isNotBlank() },
                socialUrl = prefs[KEY_SOCIAL_URL]?.takeIf { it.isNotBlank() },
                movieTimeMinutes = prefs[KEY_MOVIE_TIME_MINUTES]?.takeIf { it in 0..MAX_MINUTES },
            )
        }

    suspend fun update(profile: UserProfile) {
        dataStore.edit { prefs ->
            prefs.putOrRemove(KEY_NICKNAME, profile.nickname)
            prefs.putOrRemove(KEY_AVATAR_URI, profile.avatarUri)
            prefs.putOrRemove(KEY_SOCIAL_URL, profile.socialUrl)
            val minutes = profile.movieTimeMinutes
            if (minutes != null && minutes in 0..MAX_MINUTES) {
                prefs[KEY_MOVIE_TIME_MINUTES] = minutes
            } else {
                prefs.remove(KEY_MOVIE_TIME_MINUTES)
            }
        }
    }

    private fun androidx.datastore.preferences.core.MutablePreferences.putOrRemove(
        key: Preferences.Key<String>,
        value: String?,
    ) {
        if (value.isNullOrBlank()) remove(key) else set(key, value)
    }

    private companion object {
        const val FILE_NAME = "user_profile"
        const val MAX_MINUTES = 24 * 60 - 1
        val KEY_NICKNAME = stringPreferencesKey("nickname")
        val KEY_AVATAR_URI = stringPreferencesKey("avatar_uri")
        val KEY_SOCIAL_URL = stringPreferencesKey("social_url")
        val KEY_MOVIE_TIME_MINUTES = intPreferencesKey("movie_time_minutes")

        val Context.userProfileDataStore by preferencesDataStore(name = FILE_NAME)
    }
}
