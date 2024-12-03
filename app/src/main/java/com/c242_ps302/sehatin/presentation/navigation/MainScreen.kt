import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.c242_ps302.sehatin.presentation.navigation.Routes
import com.c242_ps302.sehatin.presentation.navigation.Routes.HealthInputScreen
import com.c242_ps302.sehatin.presentation.navigation.SehatinBottomNavigation
import com.c242_ps302.sehatin.presentation.screen.food.CameraScreen
import com.c242_ps302.sehatin.presentation.screen.food.FoodScreen
import com.c242_ps302.sehatin.presentation.screen.health_input.HealthInputScreen
import com.c242_ps302.sehatin.presentation.screen.history.HistoryScreen
import com.c242_ps302.sehatin.presentation.screen.home.HomeScreen
import com.c242_ps302.sehatin.presentation.screen.news.NewsScreen
import com.c242_ps302.sehatin.presentation.screen.settings.SettingsScreen

@Composable
fun MainScreen() { // Remove navController parameter
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
                FoodScreen(mainNavController) // Pass mainNavController here
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
            composable<Routes.CameraScreen> {
                CameraScreen(mainNavController)
            }
        }
    }
}