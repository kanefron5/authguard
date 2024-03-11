package dev.zabolotskikh.authguard.ui

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.zabolotskikh.authguard.domain.model.AppState
import dev.zabolotskikh.authguard.ui.screen.services.ServiceScreen
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsScreen
import dev.zabolotskikh.authguard.ui.screen.welcome.WelcomeScreen

sealed class Screen(private val route: String) {
    operator fun invoke() = route

    object Welcome : Screen("welcome_screen")
    object Main : Screen("main_screen")
    object Settings : Screen("settings_screen")
}

@Composable
@ExperimentalGetImage
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    state: AppState
) {
    val startDestination = if (state.isStarted) Screen.Main() else Screen.Welcome()

    fun onNavigate(screen: Screen, clear: Boolean) {
        navController.navigate(screen()) {
            if (clear) popUpTo(0)
        }
    }

    fun onNavigateBack() {
        if (navController.previousBackStackEntry != null) {
            navController.navigateUp()
        } else {
            navController.navigate(Screen.Main()) {
                popUpTo(0)
            }
        }
    }

    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(Screen.Welcome()) { WelcomeScreen() }
        composable(Screen.Settings()) {
            SettingsScreen(onNavigateBack = ::onNavigateBack)
        }
        composable(Screen.Main()) { ServiceScreen(onNavigate = ::onNavigate) }
    }
}
