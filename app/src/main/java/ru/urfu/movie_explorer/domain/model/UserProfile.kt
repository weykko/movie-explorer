package ru.urfu.movie_explorer.domain.model

/**
 * Профиль пользователя приложения.
 *
 * Все поля опциональны — пустой профиль — это профиль с `null`-полями. Так UI может
 * корректно отрисовать «пока ничего не заполнено» без отдельной модели «гостя».
 *
 * Хранится локально в DataStore Preferences (см. реализацию репозитория).
 */
data class UserProfile(
    val nickname: String?,
    val avatarUri: String?,
    val socialUrl: String?,
) {

    val isEmpty: Boolean
        get() = nickname.isNullOrBlank() && avatarUri.isNullOrBlank() && socialUrl.isNullOrBlank()

    companion object {
        val Empty: UserProfile = UserProfile(nickname = null, avatarUri = null, socialUrl = null)
    }
}
