package dev.zabolotskikh.authentificator.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.zabolotskikh.authentificator.ui.screen.services.ServiceScreen
import dev.zabolotskikh.authentificator.ui.screen.welcome.WelcomeScreen
import dev.zabolotskikh.authentificator.ui.screen.welcome.WelcomeViewModel

enum class Screen {
    WELCOME_SCREEN, MAIN_SCREEN,
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.WELCOME_SCREEN.name
) {
    val viewModel = hiltViewModel<WelcomeViewModel>()
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state.value) {
        state.value?.apply {
            if (isStarted) navController.navigate(Screen.MAIN_SCREEN.name)
            else navController.navigate(Screen.WELCOME_SCREEN.name)
        }
    }

    if (state.value != null) {
        NavHost(
            modifier = modifier, navController = navController, startDestination = startDestination
        ) {
            composable(Screen.WELCOME_SCREEN.name) { WelcomeScreen() }
            composable(Screen.MAIN_SCREEN.name) { ServiceScreen() }
        }
    }

}
