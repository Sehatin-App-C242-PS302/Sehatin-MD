package com.c242_ps302.sehatin.presentation.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.hilt.android.EntryPointAccessors
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    init {
        val appContext = context.applicationContext
        EntryPointAccessors.fromApplication(appContext, DailyReminderWorkerEntryPoint::class.java)
            .injectDailyReminderWorker(this)
    }

    override suspend fun doWork(): Result {
        return try {
            if (shouldShowNotification()) {
                showNotification()
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun shouldShowNotification(): Boolean {
        val zoneId = ZoneId.systemDefault()
        val now = ZonedDateTime.now(zoneId)

        // Set notification window from 6 AM to 11 PM
        val todaySixAM = LocalDate.now(zoneId).atTime(6, 0).atZone(zoneId)
        val todayTenPM = LocalDate.now(zoneId).atTime(23, 0).atZone(zoneId)

        // Adjust times if current time is before 6 AM
        val scheduledSixAM = if (now.isBefore(todaySixAM)) {
            todaySixAM.minusDays(1)
        } else {
            todaySixAM
        }

        val scheduledTenPM = if (now.isBefore(todaySixAM)) {
            todayTenPM.minusDays(1)
        } else {
            todayTenPM
        }

        return now.isAfter(scheduledSixAM) && now.isBefore(scheduledTenPM)
    }

    private fun showNotification() {
        val notificationHelper = DailyReminderNotificationHelper(applicationContext)
        notificationHelper.showDailyReminderNotification()
    }
}