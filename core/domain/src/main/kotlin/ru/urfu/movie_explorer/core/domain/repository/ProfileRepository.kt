package ru.urfu.movie_explorer.core.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.urfu.movie_explorer.core.domain.model.UserProfile

/**
 * Контракт хранилища профиля пользователя.
 *
 * Реализация (data-слой) использует DataStore Preferences: структура из нескольких скалярных
 * полей идеально ложится на ключ-значение, заводить ради этого Room избыточно.
 */
interface ProfileRepository {

    /** Реактивный поток текущего профиля. */
    fun observe(): Flow<UserProfile>

    /** Полностью заменяет сохранённый профиль. */
    suspend fun update(profile: UserProfile)
}
