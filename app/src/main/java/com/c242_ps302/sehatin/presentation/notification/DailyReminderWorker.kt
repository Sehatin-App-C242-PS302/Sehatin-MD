package com.c242_ps302.sehatin.presentation.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.c242_ps302.sehatin.data.repository.HealthRepository
import dagger.hilt.android.EntryPointAccessors
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class DailyReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var healthRepository: HealthRepository

    init {
        val appContext = context.applicationContext
        EntryPointAccessors.fromApplication(appContext, DailyReminderWorkerEntryPoint::class.java)
            .injectDailyReminderWorker(this)
    }

    override suspend fun doWork(): Result {
        return try {
            val lastHealthData = healthRepository.getLastHealthData()
            val lastCreatedAt = lastHealthData?.createdAt
//            if (shouldShowNotification(lastCreatedAt)) {
//                showNotification()
//            }
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

        val todayNineAM = LocalDate.now(zoneId).atTime(8, 47).atZone(zoneId)

        val scheduledNineAM = if (now.isBefore(todayNineAM)) {
            todayNineAM.minusDays(1)
        } else {
            todayNineAM
        }

        return now.isAfter(scheduledNineAM.plusDays(1))
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