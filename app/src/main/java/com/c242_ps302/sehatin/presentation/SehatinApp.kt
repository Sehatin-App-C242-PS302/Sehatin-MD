package com.c242_ps302.sehatin.presentation

import android.app.Application
import androidx.work.WorkManager
import com.c242_ps302.sehatin.presentation.notification.DailyReminderWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SehatinApp : Application() {
    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()

        DailyReminderWorker.scheduleDaily(workManager)
    }
}