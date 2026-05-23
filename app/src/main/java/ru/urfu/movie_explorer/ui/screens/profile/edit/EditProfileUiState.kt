package ru.urfu.movie_explorer.ui.screens.profile.edit

data class EditProfileUiState(
    val nickname: String = "",
    val socialUrl: String = "",
    val avatarUri: String? = null,
    val isInitialized: Boolean = false,
)
