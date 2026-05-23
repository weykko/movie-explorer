package ru.urfu.movie_explorer.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.urfu.movie_explorer.domain.model.UserProfile
import ru.urfu.movie_explorer.domain.usecase.ObserveUserProfileUseCase

/**
 * ViewModel экрана просмотра профиля.
 */
class ProfileViewModel(
    observeUserProfile: ObserveUserProfileUseCase,
) : ViewModel() {

    val profile: StateFlow<UserProfile> = observeUserProfile().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UserProfile.Empty,
    )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
