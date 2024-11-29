package com.c242_ps302.sehatin.presentation.navigation

import android.net.Uri
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.c242_ps302.sehatin.presentation.screen.food.FoodScreen
import com.c242_ps302.sehatin.presentation.screen.health.HealthInputScreen
import com.c242_ps302.sehatin.presentation.screen.health.RecommendationScreen
import com.c242_ps302.sehatin.presentation.screen.history.HistoryScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsDetailScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsScreen
import com.c242_ps302.sehatin.presentation.screen.settings.SettingsScreen

@Composable
fun MainScreen(
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val shouldShowBottomNav = when (currentRoute) {
        Routes.HOME_SCREEN, Routes.NEWS_SCREEN, Routes.FOOD_SCREEN, Routes.HISTORY_SCREEN, Routes.SETTINGS_SCREEN -> true
        else -> false
    }

    var selectedItem by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentRoute) {
        selectedItem = when (currentRoute) {
            Routes.HOME_SCREEN -> 0
            Routes.FOOD_SCREEN -> 1
            Routes.HISTORY_SCREEN -> 2
            Routes.NEWS_SCREEN -> 3
            Routes.SETTINGS_SCREEN -> 4
            else -> selectedItem
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomNav) {
                SehatinBottomNavigation(
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME_SCREEN,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(
                route = Routes.HOME_SCREEN,
                enterTransition = { slideInVertically() },
                exitTransition = { slideOutVertically() }
            ) {
                HomeScreen(
                    onFabClick = {
                        navController.navigate(Routes.HEALTH_INPUT_SCREEN)
                    }
                )
            }
            composable(
                route = Routes.FOOD_SCREEN,
                enterTransition = { slideInHorizontally() },
                exitTransition = { slideOutHorizontally() }
            ) {
                FoodScreen()
            }
            composable(
                route = Routes.HISTORY_SCREEN,
                enterTransition = { slideInHorizontally() },
                exitTransition = { slideOutHorizontally() }
            ) {
                HistoryScreen()
            }
            composable(
                route = Routes.NEWS_SCREEN,
                enterTransition = { slideInHorizontally() },
                exitTransition = { slideOutHorizontally() }
            ) {
                NewsScreen(
                    onNewsClick = { newsLink ->
                        val encodedLink = Uri.encode(newsLink)
                        navController.navigate("${Routes.NEWS_DETAIL_SCREEN}/$encodedLink")
                    }
                )
            }
            composable(
                route = Routes.SETTINGS_SCREEN,
                enterTransition = { slideInHorizontally() },
                exitTransition = { slideOutHorizontally() }
            ) {
                SettingsScreen(
                    onLogoutSuccess = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.MAIN_SCREEN) { inclusive = true }
                        }
                    }
                )
            }
            composable(
                route = Routes.HEALTH_INPUT_SCREEN,
                enterTransition = { slideInVertically() },
                exitTransition = { slideOutVertically() }
            ) {
                HealthInputScreen(
                    onBackClick = { navController.navigateUp() },
                    onSuccess = { navController.navigate(Routes.RECOMMENDATION_SCREEN) },
                    onRecommendationClick = { navController.navigate(Routes.RECOMMENDATION_SCREEN) }
                )
            }
            composable(
                route = "${Routes.NEWS_DETAIL_SCREEN}/{newsLink}",
                arguments = listOf(
                    navArgument("newsLink") { type = NavType.StringType }
                ),
                enterTransition = { slideInHorizontally() },
                exitTransition = { slideOutHorizontally() }
            ) { backStackEntry ->
                val newsLink = Uri.decode(backStackEntry.arguments?.getString("newsLink") ?: "")
                NewsDetailScreen(
                    newsLink = newsLink,
                    onBackClick = { navController.navigateUp() }
                )
            }
            composable(
                route = Routes.RECOMMENDATION_SCREEN,
                enterTransition = { slideInVertically() },
                exitTransition = { slideOutVertically() }
            ) {
                RecommendationScreen(
                    onRecountClick = { navController.navigate(Routes.HEALTH_INPUT_SCREEN) },
                    onBackClick = { navController.navigate(Routes.HOME_SCREEN) }
                )
            }
        }
    }

}