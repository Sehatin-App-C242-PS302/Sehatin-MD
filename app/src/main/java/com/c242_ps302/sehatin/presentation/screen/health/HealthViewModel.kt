package com.c242_ps302.sehatin.presentation.screen.health

import com.c242_ps302.sehatin.data.repository.HealthRepository
import javax.inject.Inject

class HealthViewModel @Inject constructor(
    private val healthRepository: HealthRepository
) {
}