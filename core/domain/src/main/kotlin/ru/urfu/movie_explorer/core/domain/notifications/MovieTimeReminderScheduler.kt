package ru.urfu.movie_explorer.core.domain.notifications

/**
 * Контракт планировщика напоминаний «время для просмотра фильма».
 *
 * UI и use case'ы работают только с этим интерфейсом, реализация (AlarmManager
 * + BroadcastReceiver) живёт в data-слое. Это позволяет в будущем подменить
 * транспорт (например, WorkManager) без правок верхних слоёв.
 */
interface MovieTimeReminderScheduler {

    /**
     * Запланировать напоминание на ближайшее наступление времени `HH:mm`
     * (если момент сегодня уже прошёл — на завтра).
     *
     * @param nickname  имя пользователя из профиля для тела уведомления.
     * @param hour      час 0..23.
     * @param minute    минута 0..59.
     */
    fun schedule(nickname: String?, hour: Int, minute: Int)

    /** Отменить ранее запланированное напоминание, если оно есть. */
    fun cancel()
}
