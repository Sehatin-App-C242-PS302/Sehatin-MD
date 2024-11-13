package com.c242_ps302.sehatin.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    // Auth Routes
    @Serializable
    data object RegisterScreen : Routes()
    @Serializable
    data object LoginScreen : Routes()

    // Main Routes
    @Serializable
    data object MainScreen : Routes()
    @Serializable
    data object HomeScreen : Routes()
    @Serializable
    data object HealthScreen : Routes()
    @Serializable
    data object HistoryScreen : Routes()
    @Serializable
    data object NewsScreen : Routes()
    @Serializable
    data object SettingsScreen : Routes()
}