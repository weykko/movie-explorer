package ru.urfu.movie_explorer.feature.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.urfu.movie_explorer.core.domain.model.UserProfile
import ru.urfu.movie_explorer.core.domain.notifications.MovieTimeReminderScheduler
import ru.urfu.movie_explorer.core.domain.usecase.ObserveUserProfileUseCase
import ru.urfu.movie_explorer.core.domain.usecase.UpdateUserProfileUseCase

/** ViewModel экрана редактирования профиля. */
class EditProfileViewModel(
    private val observeUserProfile: ObserveUserProfileUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase,
    private val reminderScheduler: MovieTimeReminderScheduler,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()

    private val _finishEvent = MutableStateFlow(false)
    val finishEvent: StateFlow<Boolean> = _finishEvent.asStateFlow()

    init {
        viewModelScope.launch {
            val saved = observeUserProfile().first()
            val hour = saved.movieTimeMinutes?.let { it / MINUTES_PER_HOUR }
            val minute = saved.movieTimeMinutes?.let { it % MINUTES_PER_HOUR }
            _uiState.value = EditProfileUiState(
                nickname = saved.nickname.orEmpty(),
                socialUrl = saved.socialUrl.orEmpty(),
                avatarUri = saved.avatarUri,
                movieTimeText = formatTime(hour, minute),
                movieTimeHour = hour,
                movieTimeMinute = minute,
                movieTimeError = false,
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

    fun onMovieTimeTextChanged(value: String) {
        val digits = value.filter { it.isDigit() }.take(MAX_DIGITS)
        if (digits.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                movieTimeText = "",
                movieTimeHour = null,
                movieTimeMinute = null,
                movieTimeError = false,
            )
            return
        }
        val formatted = if (digits.length <= HOUR_DIGITS) {
            digits
        } else {
            digits.substring(0, HOUR_DIGITS) + ":" + digits.substring(HOUR_DIGITS)
        }
        val parsed = if (digits.length == MAX_DIGITS) parseTime(formatted) else null
        _uiState.value = _uiState.value.copy(
            movieTimeText = formatted,
            movieTimeHour = parsed?.first,
            movieTimeMinute = parsed?.second,
            movieTimeError = digits.length == MAX_DIGITS && parsed == null,
        )
    }

    fun onMovieTimePicked(hour: Int, minute: Int) {
        _uiState.value = _uiState.value.copy(
            movieTimeText = formatTime(hour, minute),
            movieTimeHour = hour,
            movieTimeMinute = minute,
            movieTimeError = false,
        )
    }

    fun save() {
        val draft = _uiState.value
        if (!draft.canSave) return
        viewModelScope.launch {
            val nickname = draft.nickname.trim().takeIf { it.isNotEmpty() }
            val hour = draft.movieTimeHour
            val minute = draft.movieTimeMinute
            val totalMinutes = if (hour != null && minute != null) {
                hour * MINUTES_PER_HOUR + minute
            } else null

            updateUserProfile(
                UserProfile(
                    nickname = nickname,
                    socialUrl = draft.socialUrl.trim().takeIf { it.isNotEmpty() },
                    avatarUri = draft.avatarUri?.takeIf { it.isNotBlank() },
                    movieTimeMinutes = totalMinutes,
                ),
            )
            if (hour != null && minute != null) {
                reminderScheduler.schedule(nickname = nickname, hour = hour, minute = minute)
            } else {
                reminderScheduler.cancel()
            }
            _finishEvent.value = true
        }
    }

    fun finishConsumed() {
        _finishEvent.value = false
    }

    private fun parseTime(value: String): Pair<Int, Int>? {
        val match = TIME_REGEX.matchEntire(value) ?: return null
        val hour = match.groupValues[1].toIntOrNull() ?: return null
        val minute = match.groupValues[2].toIntOrNull() ?: return null
        if (hour !in 0..23 || minute !in 0..59) return null
        return hour to minute
    }

    private fun formatTime(hour: Int?, minute: Int?): String {
        if (hour == null || minute == null) return ""
        return "%02d:%02d".format(hour, minute)
    }

    private companion object {
        const val MINUTES_PER_HOUR = 60
        const val HOUR_DIGITS = 2
        const val MAX_DIGITS = 4
        val TIME_REGEX = Regex("^([01]?\\d|2[0-3]):([0-5]\\d)$")
    }
}
