package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.c242_ps302.sehatin.presentation.screen.auth.LoginScreen
import com.c242_ps302.sehatin.presentation.screen.auth.RegisterScreen
import com.c242_ps302.sehatin.presentation.screen.onboarding.OnboardingScreen

@Composable
fun NavGraphSetup(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.ONBOARDING
    ) {
        composable(
            route = Routes.ONBOARDING,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() }
        ) {
            OnboardingScreen(
                onLoginClick = {
                    navController.navigate(Routes.LOGIN)
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                },
                onAuthenticated = {
                    navController.navigate(Routes.MAIN_SCREEN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(
            Routes.LOGIN,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() }
        ) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Routes.MAIN_SCREEN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
        composable(
            Routes.REGISTER,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() }
        ) {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(Routes.LOGIN)
                },
                onLoginClick = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(
            Routes.MAIN_SCREEN,
            enterTransition = { slideInVertically() },
            exitTransition = { slideOutVertically() }
        ) {
            MainScreen()
        }

    }
}