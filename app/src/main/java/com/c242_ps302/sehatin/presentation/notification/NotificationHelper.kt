package com.c242_ps302.sehatin.presentation.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: SehatinAppPreferences
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_DAILY_REMINDER = "daily_reminder_channel"
        const val CHANNEL_HEALTH_ALERT = "health_alert_channel"
        const val CHANNEL_ACHIEVEMENT = "achievement_channel"

        // Notification IDs
        const val DAILY_INPUT_NOTIFICATION_ID = 1001
        const val STEP_GOAL_NOTIFICATION_ID = 1002
    }

    init {
        createNotificationChannels()
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY_REMINDER,
                "Pengingat Harian",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pengingat untuk aktivitas kesehatan harian"
            }

            val alertChannel = NotificationChannel(
                CHANNEL_HEALTH_ALERT,
                "Alert Kesehatan",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Pemberitahuan penting terkait kesehatan"
            }

            val achievementChannel = NotificationChannel(
                CHANNEL_ACHIEVEMENT,
                "Pencapaian",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pemberitahuan pencapaian dan progress"
            }

            notificationManager.createNotificationChannel(dailyChannel)
            notificationManager.createNotificationChannel(alertChannel)
            notificationManager.createNotificationChannel(achievementChannel)
        }
    }

    suspend fun sendDailyInputReminder() {
        sendNotification(
            title = "Waktu Update Data Kesehatan",
            message = "Jangan lupa update berat badan dan aktivitas hari ini!",
            channelId = CHANNEL_DAILY_REMINDER,
            notificationId = DAILY_INPUT_NOTIFICATION_ID
        )
    }

    suspend fun sendStepGoalReminder(currentSteps: Int, targetSteps: Int) {
        sendNotification(
            title = "Target Langkah Harian",
            message = "Anda sudah berjalan $currentSteps dari target $targetSteps langkah. Ayo tetap semangat!",
            channelId = CHANNEL_DAILY_REMINDER,
            notificationId = STEP_GOAL_NOTIFICATION_ID
        )
    }

    private suspend fun sendNotification(
        title: String,
        message: String,
        channelId: String,
        notificationId: Int = System.currentTimeMillis().toInt()
    ) {
        if (!hasNotificationPermission() || !preferences.getNotificationEnable()) {
            return
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notifications)
            .setPriority(
                when (channelId) {
                    CHANNEL_HEALTH_ALERT -> NotificationCompat.PRIORITY_HIGH
                    else -> NotificationCompat.PRIORITY_DEFAULT
                }
            )
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}