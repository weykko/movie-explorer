package ru.urfu.movie_explorer.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import ru.urfu.movie_explorer.domain.notifications.MovieTimeReminderScheduler
import java.util.Calendar

/**
 * Реализация [MovieTimeReminderScheduler] поверх [AlarmManager] и [MovieTimeBroadcastReceiver].
 */
class MovieTimeReminderSchedulerImpl(
    private val context: Context,
) : MovieTimeReminderScheduler {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(nickname: String?, hour: Int, minute: Int) {
        val triggerAt = nextTriggerMillis(hour, minute)
        val pending = buildPendingIntent(nickname, create = true)!!

        val useExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
        if (useExact) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        } else {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pending)
        }
    }

    override fun cancel() {
        val pending = buildPendingIntent(nickname = null, create = false) ?: return
        alarmManager.cancel(pending)
        pending.cancel()
    }

    private fun buildPendingIntent(nickname: String?, create: Boolean): PendingIntent? {
        val intent = Intent(context, MovieTimeBroadcastReceiver::class.java).apply {
            action = MovieTimeNotifications.ACTION_FIRE
            if (nickname != null) putExtra(MovieTimeNotifications.EXTRA_NICKNAME, nickname)
        }
        val flags = if (create) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        }
        return PendingIntent.getBroadcast(
            context,
            MovieTimeNotifications.ALARM_REQUEST_CODE,
            intent,
            flags,
        )
    }

    private fun nextTriggerMillis(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val target = (now.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (!target.after(now)) target.add(Calendar.DAY_OF_YEAR, 1)
        return target.timeInMillis
    }
}
