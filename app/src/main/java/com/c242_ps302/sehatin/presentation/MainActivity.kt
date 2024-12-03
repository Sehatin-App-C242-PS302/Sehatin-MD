package com.c242_ps302.sehatin.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.c242_ps302.sehatin.data.preferences.SehatinAppPreferences
import com.c242_ps302.sehatin.presentation.navigation.NavGraphSetup
import com.c242_ps302.sehatin.presentation.screen.food.CameraScreen
import com.c242_ps302.sehatin.presentation.screen.food.FoodScreen
import com.c242_ps302.sehatin.presentation.theme.SehatinTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var preferences: SehatinAppPreferences

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            val isDarkTheme by preferences.getDarkThemeFlow().collectAsState(initial = false)

            SehatinTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()
                NavGraphSetup(
                    navController = navController

                )
            }
        }
    }

    @Composable
    fun SehatinApp() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "food_screen") {
            composable("food_screen") { FoodScreen(navController = navController) }
            composable("camera_screen") { CameraScreen(navController = navController) }
        }
    }

}

