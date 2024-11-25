package com.c242_ps302.sehatin.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    // Auth Routes
    @Serializable
    data object OnboardingScreen : Routes()

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
    data object FoodScreen : Routes()

    @Serializable
    data object HistoryScreen : Routes()

    @Serializable
    data object NewsScreen : Routes()

    @Serializable
    data object SettingsScreen : Routes()

    // Child Routes
    @Serializable
    data object HealthInputScreen : Routes()

    @Serializable
    data class NewsDetailScreen(val newsLink: String) : Routes()

    @Serializable
    data object RecommendationScreen : Routes()
}