package ru.urfu.movie_explorer.domain.model

/**
 * Профиль пользователя приложения.
 */
data class UserProfile(
    val nickname: String?,
    val avatarUri: String?,
    val socialUrl: String?,
    val movieTimeMinutes: Int?,
) {

    val isEmpty: Boolean
        get() = nickname.isNullOrBlank() &&
            avatarUri.isNullOrBlank() &&
            socialUrl.isNullOrBlank() &&
            movieTimeMinutes == null

    companion object {
        val Empty: UserProfile = UserProfile(
            nickname = null,
            avatarUri = null,
            socialUrl = null,
            movieTimeMinutes = null,
        )
    }
}
