package ru.urfu.movie_explorer.ui.screens.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.domain.model.UserProfile
import ru.urfu.movie_explorer.domain.usecase.ObserveUserProfileUseCase
import ru.urfu.movie_explorer.domain.usecase.UpdateUserProfileUseCase

/**
 * ViewModel экрана редактирования профиля.
 */
class EditProfileViewModel(
    private val observeUserProfile: ObserveUserProfileUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _finishEvent = MutableStateFlow(false)
    val finishEvent: StateFlow<Boolean> = _finishEvent.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = observeUserProfile().first()
            _uiState.value = EditProfileUiState(
                nickname = saved.nickname.orEmpty(),
                socialUrl = saved.socialUrl.orEmpty(),
                avatarUri = saved.avatarUri,
                isInitialized = true,
            )
        }
    }

    fun onNicknameChanged(value: String) {
        _uiState.value = _uiState.value.copy(nickname = value)
    }

    fun onSocialUrlChanged(value: String) {
        _uiState.value = _uiState.value.copy(socialUrl = value)
    }

    fun onAvatarChanged(uri: String?) {
        _uiState.value = _uiState.value.copy(avatarUri = uri)
    }

    fun save() {
        val draft = _uiState.value
        viewModelScope.launch {
            updateUserProfile(
                UserProfile(
                    nickname = draft.nickname.trim().takeIf { it.isNotEmpty() },
                    socialUrl = draft.socialUrl.trim().takeIf { it.isNotEmpty() },
                    avatarUri = draft.avatarUri?.takeIf { it.isNotBlank() },
                ),
            )
            _finishEvent.value = true
        }
    }

    fun finishConsumed() {
        _finishEvent.value = false
    }
}
