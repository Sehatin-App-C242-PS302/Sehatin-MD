package com.c242_ps302.sehatin.ui.navigation

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.c242_ps302.sehatin.R
import com.c242_ps302.sehatin.ui.screen.auth.LoginScreen
import com.c242_ps302.sehatin.ui.screen.auth.RegisterScreen
import com.c242_ps302.sehatin.ui.screen.connectivity.ConnectivityViewModel
import com.c242_ps302.sehatin.ui.screen.connectivity.NetworkStatusBar
import com.c242_ps302.sehatin.ui.screen.food.input.FoodInputScreen
import com.c242_ps302.sehatin.ui.screen.food.result.FoodResultScreen
import com.c242_ps302.sehatin.ui.screen.health.input.HealthInputScreen
import com.c242_ps302.sehatin.ui.screen.health.result.HealthResultScreen
import com.c242_ps302.sehatin.ui.screen.history.HistoryScreen
import com.c242_ps302.sehatin.ui.screen.home.HomeScreen
import com.c242_ps302.sehatin.ui.screen.news.NewsDetailScreen
import com.c242_ps302.sehatin.ui.screen.news.NewsScreen
import com.c242_ps302.sehatin.ui.screen.onboarding.OnboardingScreen
import com.c242_ps302.sehatin.ui.screen.profile.ProfileScreen
import com.c242_ps302.sehatin.ui.screen.settings.SettingsScreen
import com.c242_ps302.sehatin.ui.theme.customGreen
import kotlinx.coroutines.delay

@Composable
fun NavGraphSetup(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = SubGraph.Auth
    ) {
        // Auth Navigation Graph
        navigation<SubGraph.Auth>(startDestination = Dest.Onboarding) {
            composable<Dest.Onboarding> {
                OnboardingScreen(
                    onLoginClick = {
                        navController.navigate(Dest.Login)
                    },
                    onRegisterClick = {
                        navController.navigate(Dest.Register)
                    },
                    onAuthenticated = {
                        navController.navigate(SubGraph.Main) {
                            popUpTo(Dest.Onboarding) { inclusive = true }
                        }
                    }
                )
            }

            composable<Dest.Login> {
                LoginScreen(
                    onLoginClick = {
                        navController.navigate(SubGraph.Main) {
                            popUpTo(Dest.Login) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Dest.Register)
                    }
                )
            }

            composable<Dest.Register> {
                RegisterScreen(
                    onRegisterClick = {
                        navController.navigate(Dest.Login)
                    },
                    onLoginClick = {
                        navController.navigate(Dest.Login)
                    }
                )
            }
        }

        // Main Navigation Graph
        navigation<SubGraph.Main>(startDestination = Dest.Home) {
            composable<Dest.Home> {
                MainScreenContent(
                    navController = navController,
                    startDestination = Dest.Home
                )
            }

            composable<Dest.FoodInput> {
                MainScreenContent(
                    navController = navController,
                    startDestination = Dest.FoodInput
                )
            }

            composable<Dest.History> {
                MainScreenContent(
                    navController = navController,
                    startDestination = Dest.History
                )
            }

            composable<Dest.News> {
                MainScreenContent(
                    navController = navController,
                    startDestination = Dest.News
                )
            }

            composable<Dest.Settings> {
                MainScreenContent(
                    navController = navController,
                    startDestination = Dest.Settings
                )
            }

            composable<Dest.HealthInput> {
                HealthInputScreen(
                    onNavigateUp = { navController.navigate(Dest.Home) },
                    onSuccess = { navController.navigate(Dest.HealthResult) }
                )
            }

            composable<Dest.NewsDetail> { backStackEntry ->
                val newsLink = backStackEntry.toRoute<Dest.NewsDetail>().newsLink
                NewsDetailScreen(
                    newsLink = newsLink,
                    onNavigateUp = { navController.navigate(Dest.News) }
                )
            }

            composable<Dest.HealthResult> {
                HealthResultScreen(
                    onRecountClick = { navController.navigate(Dest.HealthInput) },
                    onBackClick = { navController.navigate(Dest.Home) }
                )
            }

            composable<Dest.Profile> {
                ProfileScreen(
                    onNavigateUp = { navController.navigate(Dest.Settings) }
                )
            }

            composable<Dest.FoodResult> {
                FoodResultScreen(
                    onNavigateHome = { navController.navigate(Dest.Home) }
                )
            }
        }
    }
}

// Modifikasi MainScreenContent untuk menerima startDestination
@Composable
fun MainScreenContent(
    navController: NavHostController,
    connectivityObserver: ConnectivityViewModel = hiltViewModel(),
    startDestination: Dest,
) {
    var selectedIndex by remember {
        mutableIntStateOf(
            when (startDestination) {
                Dest.Home -> 0
                Dest.FoodInput -> 1
                Dest.History -> 2
                Dest.News -> 3
                Dest.Settings -> 4
                else -> 0
            }
        )
    }

    val context = LocalContext.current

    val status by connectivityObserver.isConnected.collectAsStateWithLifecycle()
    var showMessageBar by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(Color.Red) }

    LaunchedEffect(key1 = status) {
        when (status) {
            true -> {
                message = context.getString(R.string.connected_to_internet)
                backgroundColor = customGreen
                delay(2000)
                showMessageBar = false
            }

            false -> {
                showMessageBar = true
                message = context.getString(R.string.no_internet_connection)
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
            selectedIndex = selectedIndex,
        )
    }
}

// Modifikasi ContentScreen untuk menggunakan Dest
@Composable
fun ContentScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier.fillMaxSize(),
    selectedIndex: Int,
) {
    when (selectedIndex) {
        0 -> HomeScreen(
            onFabClick = {
                navController.navigate(Dest.HealthInput)
            },
            modifier = modifier
        )

        1 -> FoodInputScreen(
            onNavigateToResult = {
                navController.navigate(Dest.FoodResult)
            },
            modifier = modifier
        )

        2 -> HistoryScreen(
            modifier = modifier
        )

        3 -> NewsScreen(
            onNewsClick = { newsLink ->
                navController.navigate(Dest.NewsDetail(newsLink))
            },
            modifier = modifier
        )

        4 -> SettingsScreen(
            onLogoutSuccess = {
                navController.navigate(Dest.Login) {
                    popUpTo(SubGraph.Main) { inclusive = true }
                }
            },
            onProfileClick = {
                navController.navigate(Dest.Profile)
            },
            modifier = modifier
        )
    }
}