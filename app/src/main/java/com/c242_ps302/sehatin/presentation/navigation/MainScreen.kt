package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.c242_ps302.sehatin.presentation.screen.food.FoodScreen
import com.c242_ps302.sehatin.presentation.screen.health_input.HealthInputScreen
import com.c242_ps302.sehatin.presentation.screen.history.HistoryScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsScreen
import com.c242_ps302.sehatin.presentation.screen.settings.SettingsScreen

@Composable
fun MainScreen(
    navController: NavHostController,
) {
    val mainNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            SehatinBottomNavigation(
                navController = mainNavController
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = mainNavController,
            startDestination = Routes.HomeScreen,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable<Routes.HomeScreen> {
                HomeScreen(
                    onFabClick = {
                        mainNavController.navigate(Routes.HealthInputScreen)
                    }
                )
            }
            composable<Routes.FoodScreen> {
                FoodScreen()
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
            composable<Routes.HealthInputScreen> {
                HealthInputScreen()
            }
        }
    }

}