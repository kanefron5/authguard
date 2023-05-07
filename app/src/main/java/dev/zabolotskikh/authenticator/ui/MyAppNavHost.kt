package dev.zabolotskikh.authenticator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.zabolotskikh.authenticator.ui.screen.services.ServiceScreen
import dev.zabolotskikh.authenticator.ui.screen.welcome.WelcomeScreen
import dev.zabolotskikh.authenticator.ui.screen.welcome.WelcomeViewModel

sealed class Screen(val route: String) {
    object Welcome: Screen("welcome_screen")
    object Main: Screen("main_screen")
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route
) {
    val viewModel = hiltViewModel<WelcomeViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        state?.apply {
            if (isStarted) navController.navigate(Screen.Main.route)
            else navController.navigate(Screen.Welcome.route)
        }
    }

    if (state != null) {
        NavHost(
            modifier = modifier, navController = navController, startDestination = startDestination
        ) {
            composable(Screen.Welcome.route) { WelcomeScreen() }
            composable(Screen.Main.route) { ServiceScreen() }
        }
    }

}
