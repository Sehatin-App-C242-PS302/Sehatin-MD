package com.c242_ps302.sehatin.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.c242_ps302.sehatin.R

data class BottomNavigationItem(
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: Any
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        titleResId = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = HomeScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.food,
        selectedIcon = Icons.Filled.Fastfood,
        unselectedIcon = Icons.Outlined.Fastfood,
        route = FoodInputScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.history,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
        route = HistoryScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.news,
        selectedIcon = Icons.AutoMirrored.Filled.Article,
        unselectedIcon = Icons.AutoMirrored.Outlined.Article,
        route = NewsScreen
    ),
    BottomNavigationItem(
        titleResId = R.string.settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        route = SettingsScreen
    )
)