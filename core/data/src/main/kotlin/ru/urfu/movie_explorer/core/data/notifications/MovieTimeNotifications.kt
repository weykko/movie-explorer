package ru.urfu.movie_explorer.core.data.notifications

/** Константы слоя уведомлений «Время для просмотра фильма». */
internal object MovieTimeNotifications {

    const val CHANNEL_ID = "movie_time_reminder"
    const val NOTIFICATION_ID = 1001
    const val ALARM_REQUEST_CODE = 2001
    const val ACTION_FIRE = "ru.urfu.movie_explorer.action.MOVIE_TIME_FIRE"
    const val EXTRA_NICKNAME = "nickname"
}
