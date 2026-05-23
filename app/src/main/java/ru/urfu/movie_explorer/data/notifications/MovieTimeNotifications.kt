package ru.urfu.movie_explorer.data.notifications

/**
 * Константы слоя уведомлений «Время для просмотра фильма».
 */
internal object MovieTimeNotifications {

    /** ID канала уведомлений (Android 8+). */
    const val CHANNEL_ID = "movie_time_reminder"

    /** ID для NotificationManager.notify — фиксированный, чтобы перезатирать прошлое. */
    const val NOTIFICATION_ID = 1001

    /** ID для AlarmManager PendingIntent — фиксированный, чтобы можно было отменить. */
    const val ALARM_REQUEST_CODE = 2001

    /** Кастомный action ресивера. */
    const val ACTION_FIRE = "ru.urfu.movie_explorer.action.MOVIE_TIME_FIRE"

    /** Extra с никнеймом для тела уведомления. */
    const val EXTRA_NICKNAME = "nickname"
}
