package com.c242_ps302.sehatin.ui.utils

import androidx.compose.material3.SnackbarDuration

data class SnackbarEvent(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short
)
