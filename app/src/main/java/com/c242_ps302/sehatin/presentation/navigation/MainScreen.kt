package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.c242_ps302.sehatin.presentation.screen.food.FoodScreen
import com.c242_ps302.sehatin.presentation.screen.health.HealthInputScreen
import com.c242_ps302.sehatin.presentation.screen.history.HistoryScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsDetailScreen
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
                NewsScreen(
                    onNewsClick = { newsLink ->
                        mainNavController.navigate(Routes.NewsDetailScreen(newsLink))
                    }
                )
            }
            composable<Routes.SettingsScreen> {
                SettingsScreen(
                    onLogoutSuccess = {
                        navController.navigate(Routes.OnboardingScreen) {
                            popUpTo(Routes.MainScreen) { inclusive = true }
                        }
                    }
                )
            }
            composable<Routes.HealthInputScreen> {
                HealthInputScreen(
                    onBackClick = { navController.navigateUp() }
                )
            }
            composable<Routes.NewsDetailScreen> { backStackEntry ->
                val newsLink = backStackEntry.toRoute<Routes.NewsDetailScreen>().newsLink
                NewsDetailScreen(
                    newsLink = newsLink,
                    onBackClick = { navController.navigateUp() }
                )
            }
        }
    }

}