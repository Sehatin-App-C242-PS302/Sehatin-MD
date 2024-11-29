package com.c242_ps302.sehatin.presentation.navigation

import kotlinx.serialization.Serializable

// Routes
@Serializable object OnboardingScreen
@Serializable object RegisterScreen
@Serializable object LoginScreen

// Routes for nested graph
@Serializable object MainScreen

// Routes inside nested graph
@Serializable object HomeScreen
@Serializable object FoodScreen
@Serializable object HistoryScreen
@Serializable object NewsScreen
@Serializable object SettingsScreen
@Serializable object HealthInputScreen
@Serializable object RecommendationScreen

@Serializable
data class NewsDetailScreen(val newsLink: String)
