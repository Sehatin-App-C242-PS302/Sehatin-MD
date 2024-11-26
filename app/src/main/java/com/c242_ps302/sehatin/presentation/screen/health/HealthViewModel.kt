package com.c242_ps302.sehatin.presentation.screen.health

import com.c242_ps302.sehatin.data.repository.HistoryRepository
import javax.inject.Inject

class HealthViewModel @Inject constructor(
    private val healthRepository: HistoryRepository
) {
}