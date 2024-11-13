package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.c242_ps302.sehatin.presentation.screen.auth.LoginScreen
import com.c242_ps302.sehatin.presentation.screen.auth.RegisterScreen

@Composable
fun NavGraphSetup(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LoginScreen
    ) {
        composable<Routes.LoginScreen> {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Routes.MainScreen) {
                        popUpTo(Routes.LoginScreen) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.RegisterScreen)
                }
            )
        }
        composable<Routes.RegisterScreen> {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(Routes.MainScreen) {
                        popUpTo(Routes.RegisterScreen) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.MainScreen> {
            MainScreen(navController = navController)
        }
    }
}