package ru.urfu.movie_explorer.feature.profile.edit

data class EditProfileUiState(
    val nickname: String = "",
    val socialUrl: String = "",
    val avatarUri: String? = null,
    val movieTimeText: String = "",
    val movieTimeHour: Int? = null,
    val movieTimeMinute: Int? = null,
    val movieTimeError: Boolean = false,
    val isInitialized: Boolean = false,
) {
    val canSave: Boolean
        get() = isInitialized && !movieTimeError
}
