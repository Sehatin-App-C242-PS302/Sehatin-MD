package com.c242_ps302.sehatin.presentation.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        notificationHelper.sendDailyInputReminder()
        return Result.success()
    }

    companion object {
        fun scheduleDaily(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()

            val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(
                1, TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .setInitialDelay(
                    calculateInitialDelay(),
                    TimeUnit.MILLISECONDS
                )
                .build()

            workManager.enqueueUniquePeriodicWork(
                "daily_reminder",
                ExistingPeriodicWorkPolicy.UPDATE,
                dailyWorkRequest
            )
        }

        private fun calculateInitialDelay(): Long {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 7)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            if (calendar.timeInMillis <= System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            return calendar.timeInMillis - System.currentTimeMillis()
        }

        fun scheduleImmediateReminder(workManager: WorkManager) {
            val immediateWorkRequest = OneTimeWorkRequestBuilder<DailyReminderWorker>()
                .setInitialDelay(0, TimeUnit.SECONDS)
                .build()

            workManager.enqueue(immediateWorkRequest)
        }
    }
}