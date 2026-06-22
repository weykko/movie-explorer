package ru.urfu.movie_explorer.core.data.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.core.data.local.preferences.UserProfileDataStore
import ru.urfu.movie_explorer.core.domain.model.UserProfile
import ru.urfu.movie_explorer.core.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val dataStore: UserProfileDataStore,
) : ProfileRepository {

    override fun observe(): Flow<UserProfile> = dataStore.profile

    override suspend fun update(profile: UserProfile) {
        dataStore.update(profile)
    }
}
