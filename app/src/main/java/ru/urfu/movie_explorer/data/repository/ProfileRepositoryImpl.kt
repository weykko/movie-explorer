package ru.urfu.movie_explorer.data.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.data.local.preferences.UserProfileDataStore
import ru.urfu.movie_explorer.domain.model.UserProfile
import ru.urfu.movie_explorer.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val dataStore: UserProfileDataStore,
) : ProfileRepository {

    override fun observe(): Flow<UserProfile> = dataStore.profile

    override suspend fun update(profile: UserProfile) {
        dataStore.update(profile)
    }
}
