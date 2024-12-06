package com.c242_ps302.sehatin.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.presentation.MainActivity

class DailyReminderNotificationHelper(private val context: Context) {
    companion object {
        private const val CHANNEL_ID = "daily_reminder_channel"
        private const val NOTIFICATION_ID = 714925
        const val DAILY_REMINDER_WORK_NAME = "DAILY_REMINDER_WORK"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Daily Health Data Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Daily reminder to input your health data"
            enableLights(true)
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun showDailyReminderNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_sehatin)
            .setContentTitle(context.getString(R.string.daily_reminder_title_health))
            .setContentText(context.getString(R.string.daily_reminder_content_health))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}