package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.c242_ps302.sehatin.R

data class BottomNavigationItem(
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Routes
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        titleResId = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = Routes.HomeScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.food,
        selectedIcon = Icons.Filled.Fastfood,
        unselectedIcon = Icons.Outlined.Fastfood,
        route = Routes.FoodScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.history,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        route = Routes.HistoryScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.news,
        selectedIcon = Icons.Filled.Newspaper,
        unselectedIcon = Icons.Outlined.Newspaper,
        route = Routes.NewsScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = Routes.SettingsScreen
    )
)
