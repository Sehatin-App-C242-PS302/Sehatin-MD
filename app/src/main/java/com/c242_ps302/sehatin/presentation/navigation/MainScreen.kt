package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.c242_ps302.sehatin.presentation.screen.health.HealthScreen
import com.c242_ps302.sehatin.presentation.screen.history.HistoryScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsViewModel
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
                val newsViewModel: NewsViewModel = hiltViewModel()
                val newsList = newsViewModel.newsList.collectAsStateWithLifecycle()

                LaunchedEffect(Unit) {
                    newsViewModel.getHeadlineNews()
                }

                NewsScreen(
                    newsList = newsList.value,
                    newsViewModel = newsViewModel
                )
            }
            composable<Routes.SettingsScreen> {
                SettingsScreen()
            }
        }
    }

}