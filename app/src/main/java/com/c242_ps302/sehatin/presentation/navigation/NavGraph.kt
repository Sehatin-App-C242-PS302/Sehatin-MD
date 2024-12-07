package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.c242_ps302.sehatin.presentation.screen.auth.LoginScreen
import com.c242_ps302.sehatin.presentation.screen.auth.RegisterScreen
import com.c242_ps302.sehatin.presentation.screen.connectivity.ConnectivityViewModel
import com.c242_ps302.sehatin.presentation.screen.connectivity.NetworkStatusBar
import com.c242_ps302.sehatin.presentation.screen.food.input.FoodInputScreen
import com.c242_ps302.sehatin.presentation.screen.food.result.FoodResultScreen
import com.c242_ps302.sehatin.presentation.screen.health.input.HealthInputScreen
import com.c242_ps302.sehatin.presentation.screen.health.result.HealthResultScreen
import com.c242_ps302.sehatin.presentation.screen.history.HistoryScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsDetailScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsScreen
import com.c242_ps302.sehatin.presentation.screen.onboarding.OnboardingScreen
import com.c242_ps302.sehatin.presentation.screen.profile.ProfileScreen
import com.c242_ps302.sehatin.presentation.screen.settings.SettingsScreen
import com.c242_ps302.sehatin.presentation.theme.customGreen
import kotlinx.coroutines.delay

@Composable
fun NavGraphSetup(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingScreen
    ) {
        composable<OnboardingScreen> {
            OnboardingScreen(
                onLoginClick = {
                    navController.navigate(LoginScreen)
                },
                onRegisterClick = {
                    navController.navigate(RegisterScreen)
                },
                onAuthenticated = {
                    navController.navigate(MainScreen) {
                        popUpTo(OnboardingScreen) { inclusive = true }
                    }
                }
            )
        }
        composable<LoginScreen> {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(MainScreen) {
                        popUpTo(LoginScreen) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(RegisterScreen)
                }
            )
        }
        composable<RegisterScreen> {
            RegisterScreen(
                onRegisterClick = {
                    navController.navigate(LoginScreen)
                },
                onLoginClick = {
                    navController.navigate(LoginScreen)
                }
            )
        }
        navigation<MainScreen>(startDestination = HomeScreen) {
            composable<HomeScreen> { MainScreenContent(navController) }
            composable<FoodInputScreen> { MainScreenContent(navController) }
            composable<HistoryScreen> { MainScreenContent(navController) }
            composable<NewsScreen> { MainScreenContent(navController) }
            composable<SettingsScreen> { MainScreenContent(navController) }
            composable<HealthInputScreen> {
                HealthInputScreen(
                    onNavigateUp = { navController.navigateUp() },
                    onSuccess = { navController.navigate(HealthResultScreen) },
                )
            }
            composable<NewsDetailScreen> { backStackEntry ->
                val newsLink = backStackEntry.toRoute<NewsDetailScreen>().newsLink
                NewsDetailScreen(
                    newsLink = newsLink,
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable<HealthResultScreen> {
                HealthResultScreen(
                    onRecountClick = { navController.navigate(HealthInputScreen) },
                    onBackClick = { navController.navigate(HomeScreen) }
                )
            }
            composable<ProfileScreen> {
                ProfileScreen(
                    onNavigateUp = { navController.navigateUp() }
                )
            }
            composable<FoodResultScreen> {
                FoodResultScreen(
                    onNavigateHome = { navController.navigate(HomeScreen) }
                )
            }
        }
    }
}

@Composable
fun MainScreenContent(
    navController: NavHostController,
    connectivityObserver: ConnectivityViewModel = hiltViewModel(),
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    val status by connectivityObserver.isConnected.collectAsStateWithLifecycle()
    var showMessageBar by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(Color.Red) }

    LaunchedEffect(key1 = status) {
        when (status) {
            true -> {
                message = "Connected to internet"
                backgroundColor = customGreen
                delay(2000)
                showMessageBar = false
            }

            false -> {
                showMessageBar = true
                message = "No internet connection"
                backgroundColor = Color.Red
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NetworkStatusBar(
                showMessageBar = showMessageBar,
                message = message,
                backgroundColor = backgroundColor
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedIndex == index) navItem.selectedIcon else navItem.unselectedIcon,
                                contentDescription = "Icon"
                            )
                        },
                        label = {
                            Text(text = stringResource(id = navItem.titleResId))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            navController = navController,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            selectedIndex,
        )
    }
}


@Composable
fun ContentScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier.fillMaxSize(),
    selectedIndex: Int,
) {
    when (selectedIndex) {
        0 -> HomeScreen(
            onFabClick = {
                navController.navigate(HealthInputScreen)
            },
            modifier = modifier
        )

        1 -> FoodInputScreen(
            onNavigateToResult = {
                navController.navigate(FoodResultScreen)
            },
            modifier = modifier
        )

        2 -> HistoryScreen(
            modifier = modifier
        )

        3 -> NewsScreen(
            onNewsClick = { newsLink ->
                navController.navigate(NewsDetailScreen(newsLink))
            },
            modifier = modifier
        )

        4 -> SettingsScreen(
            onLogoutSuccess = {
                navController.navigate(LoginScreen) {
                    popUpTo(MainScreen) { inclusive = true }
                }
            },
            onProfileClick = {
                navController.navigate(ProfileScreen)
            },
            modifier = modifier
        )
    }
}