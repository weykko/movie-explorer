package ru.urfu.movie_explorer.core.data.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import ru.urfu.movie_explorer.core.data.R

/**
 * Принимает срабатывание AlarmManager и публикует уведомление о «времени для просмотра фильма».
 *
 * При нажатии открывает приложение через стандартный launch-intent (без прямой зависимости
 * на `MainActivity` из модуля `app`).
 */
class MovieTimeBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != MovieTimeNotifications.ACTION_FIRE) return

        ensureChannel(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) return
        }

        val nickname = intent.getStringExtra(MovieTimeNotifications.EXTRA_NICKNAME)
            ?.takeIf { it.isNotBlank() }
        val title = context.getString(R.string.movie_time_notification_title)
        val text = if (nickname != null) {
            context.getString(R.string.movie_time_notification_text_named, nickname)
        } else {
            context.getString(R.string.movie_time_notification_text_anonymous)
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP }
        val pendingContent = launchIntent?.let {
            PendingIntent.getActivity(
                context,
                MovieTimeNotifications.NOTIFICATION_ID,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        val notification = NotificationCompat.Builder(context, MovieTimeNotifications.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .apply { if (pendingContent != null) setContentIntent(pendingContent) }
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(MovieTimeNotifications.NOTIFICATION_ID, notification)
    }

    private fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (manager.getNotificationChannel(MovieTimeNotifications.CHANNEL_ID) != null) return
        val channel = NotificationChannel(
            MovieTimeNotifications.CHANNEL_ID,
            context.getString(R.string.movie_time_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = context.getString(R.string.movie_time_channel_description)
        }
        manager.createNotificationChannel(channel)
    }
}
