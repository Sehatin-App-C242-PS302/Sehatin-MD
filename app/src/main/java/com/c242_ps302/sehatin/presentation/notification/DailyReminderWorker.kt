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

        // Waktu sekarang
        val now = ZonedDateTime.now(zoneId)

        // Jadwal pukul 7 pagi hari ini
        val todaySevenAM = LocalDate.now(zoneId).atTime(7, 0).atZone(zoneId)

        // Jika sekarang sebelum 7 pagi, gunakan jadwal 7 pagi kemarin
        val scheduledSevenAM = if (now.isBefore(todaySevenAM)) {
            todaySevenAM.minusDays(1)
        } else {
            todaySevenAM
        }

        // Cek apakah sekarang berada dalam range waktu (misal, hingga jam 9 pagi)
        val notificationWindowEnd = scheduledSevenAM.plusHours(2) // Hingga jam 9 pagi
        return now.isAfter(scheduledSevenAM) && now.isBefore(notificationWindowEnd)
    }


//    private fun shouldShowNotification(lastCreatedAt: String?): Boolean {
//        if (lastCreatedAt == null) return true
//
//        val lastCreatedAtInstant = try {
//            Instant.parse(lastCreatedAt)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return true
//        }
//
//        val currentInstant = Instant.now()
//        val oneDayDuration = Duration.ofDays(1)
//
//        return Duration.between(lastCreatedAtInstant, currentInstant) >= oneDayDuration
//    }

    private fun showNotification() {
        val notificationHelper = DailyReminderNotificationHelper(applicationContext)
        notificationHelper.showDailyReminderNotification()
    }
}