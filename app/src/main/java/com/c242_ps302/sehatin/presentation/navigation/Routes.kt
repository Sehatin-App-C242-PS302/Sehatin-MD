package com.c242_ps302.sehatin.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {

    @Serializable
    data object RegisterScreen : Routes()

    @Serializable
    data object LoginScreen : Routes()

    @Serializable
    data object HomeScreen : Routes()
}