package dev.zabolotskikh.authguard.ui

import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.zabolotskikh.authguard.ui.screen.services.ServiceScreen
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsScreen
import dev.zabolotskikh.authguard.ui.screen.welcome.WelcomeScreen
import dev.zabolotskikh.authguard.ui.screen.welcome.WelcomeViewModel

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")
    object Main : Screen("main_screen")
    object Settings : Screen("settings_screen")
}

@Composable
@ExperimentalGetImage
fun MyAppNavHost(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    val viewModel = hiltViewModel<WelcomeViewModel>()
    val state by viewModel.state.collectAsState()
    val startDestination = if (state?.isStarted == true) Screen.Main.route else Screen.Welcome.route

    fun onNavigate(screen: Screen, clear: Boolean) {
        navController.navigate(screen.route) {
            if (clear) popUpTo(0)
        }
    }

    fun onNavigateBack() {
        if (navController.previousBackStackEntry != null) {
            navController.navigateUp()
        } else {
            navController.navigate(Screen.Main.route) {
                popUpTo(0)
            }
        }
    }

    if (state != null) {
        NavHost(
            modifier = modifier, navController = navController, startDestination = startDestination
        ) {
            composable(Screen.Welcome.route) { WelcomeScreen() }
            composable(Screen.Settings.route) { SettingsScreen(onNavigateBack = ::onNavigateBack) }
            composable(Screen.Main.route) { ServiceScreen(onNavigate = ::onNavigate) }
        }
    }
}