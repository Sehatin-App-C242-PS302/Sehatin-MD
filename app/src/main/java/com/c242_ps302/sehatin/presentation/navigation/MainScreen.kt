package com.c242_ps302.sehatin.presentation.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.c242_ps302.sehatin.presentation.screen.health.HealthScreen
import com.c242_ps302.sehatin.presentation.screen.history.HistoryScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsScreen
import com.c242_ps302.sehatin.presentation.screen.settings.SettingsScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController
) {
    val mainNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            SehatinBottomNavigation(
                navController = mainNavController
            )
        }
    ) {
        NavHost(
            navController = mainNavController,
            startDestination = Routes.HomeScreen,
            modifier = Modifier.padding()
        ) {
            composable<Routes.HomeScreen> {
                HomeScreen(
                    onLogoutClick = {
                        navController.navigate(Routes.LoginScreen) {
                            popUpTo(Routes.MainScreen) { inclusive = true }
                        }
                    },
                )
            }
            composable<Routes.HealthScreen> {
                HealthScreen()
            }
            composable<Routes.HistoryScreen> {
                HistoryScreen()
            }
            composable<Routes.NewsScreen> {
                NewsScreen()
            }
            composable<Routes.SettingsScreen> {
                SettingsScreen()
            }
        }
    }
}