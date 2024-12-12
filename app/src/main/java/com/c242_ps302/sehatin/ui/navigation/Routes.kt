package com.c242_ps302.sehatin.ui.navigation

import kotlinx.serialization.Serializable

// SubGraph dan Routes
sealed class SubGraph {
    @Serializable
    data object Auth : SubGraph()

    @Serializable
    data object Main : SubGraph()
}

sealed class Dest {
    // Auth Routes
    @Serializable
    data object Onboarding : Dest()

    @Serializable
    data object Login : Dest()

    @Serializable
    data object Register : Dest()

    // Main Routes
    @Serializable
    data object Home : Dest()

    @Serializable
    data object FoodInput : Dest()

    @Serializable
    data object FoodResult : Dest()

    @Serializable
    data object HealthInput : Dest()

    @Serializable
    data object HealthResult : Dest()

    @Serializable
    data object History : Dest()

    @Serializable
    data object News : Dest()

    @Serializable
    data class NewsDetail(val newsLink: String) : Dest()

    @Serializable
    data object Settings : Dest()

    @Serializable
    data object Profile : Dest()
}
