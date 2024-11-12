package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.c242_ps302.sehatin.presentation.screen.auth.LoginScreen
import com.c242_ps302.sehatin.presentation.screen.auth.RegisterScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen

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
                onLoginClick = { navController.navigate(Routes.HomeScreen) },
            )
        }
        composable<Routes.RegisterScreen> {
            RegisterScreen(
                onRegisterClick = { navController.navigate(Routes.HomeScreen) },
            )
        }
        composable<Routes.HomeScreen> {
            HomeScreen(
                onLoginClick = { navController.navigate(Routes.LoginScreen) },
                onRegisterClick = { navController.navigate(Routes.RegisterScreen) },
            )
        }
    }
}