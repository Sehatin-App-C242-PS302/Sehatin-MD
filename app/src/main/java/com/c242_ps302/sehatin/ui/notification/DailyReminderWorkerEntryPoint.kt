package com.c242_ps302.sehatin.ui.notification

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DailyReminderWorkerEntryPoint {
    fun injectDailyReminderWorker(worker: DailyReminderWorker)
}