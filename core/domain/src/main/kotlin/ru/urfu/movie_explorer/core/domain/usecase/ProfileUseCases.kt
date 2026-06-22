package ru.urfu.movie_explorer.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.core.domain.model.UserProfile
import ru.urfu.movie_explorer.core.domain.repository.ProfileRepository

class ObserveUserProfileUseCase(
    private val repository: ProfileRepository,
) {
    operator fun invoke(): Flow<UserProfile> = repository.observe()
}

class UpdateUserProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.update(profile)
    }
}
