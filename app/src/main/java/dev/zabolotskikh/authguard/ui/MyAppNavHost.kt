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
import dev.zabolotskikh.authguard.ui.screen.passkey.PasskeyScreen
import dev.zabolotskikh.authguard.ui.screen.services.ServiceScreen
import dev.zabolotskikh.authguard.ui.screen.settings.SettingsScreen
import dev.zabolotskikh.authguard.ui.screen.welcome.WelcomeScreen
import dev.zabolotskikh.authguard.ui.screen.welcome.WelcomeViewModel

sealed class Screen(private val route: String) {
    operator fun invoke() = route

    object Welcome : Screen("welcome_screen")
    object Main : Screen("main_screen")
    object Settings : Screen("settings_screen")
    object PassKey : Screen("password_screen")
}

@Composable
@ExperimentalGetImage
fun MyAppNavHost(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    val viewModel = hiltViewModel<WelcomeViewModel>()
    val state by viewModel.state.collectAsState()
    val startDestination = if (state?.isStarted == true) Screen.Main() else Screen.Welcome()

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

    if (state != null) {
        NavHost(
            modifier = modifier, navController = navController, startDestination = startDestination
        ) {
            composable(Screen.Welcome()) { WelcomeScreen() }
            composable(Screen.Settings()) { SettingsScreen(onNavigateBack = ::onNavigateBack) }
            composable(Screen.Main()) { ServiceScreen(onNavigate = ::onNavigate) }
            composable(Screen.PassKey()) { PasskeyScreen() }
        }
    }
}
