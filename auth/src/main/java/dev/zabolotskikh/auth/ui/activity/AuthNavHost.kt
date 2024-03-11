package dev.zabolotskikh.auth.ui.activity

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.zabolotskikh.auth.ui.activity.AuthActivity.AuthAction.SignIn
import dev.zabolotskikh.auth.ui.activity.AuthActivity.AuthAction.SignUp
import dev.zabolotskikh.auth.ui.activity.auth.AuthScreen

internal sealed class Screen(private val route: String) {
    operator fun invoke() = route

    object SignIn : Screen("sign_in_screen")
    object SignUp : Screen("sign_up_screen")
}

@Composable
internal fun AuthNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    action: AuthActivity.AuthAction,
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
) {
    val startDestination = when (action) {
        is SignIn -> Screen.SignIn()
        is SignUp -> Screen.SignUp()
    }

    fun onNavigate(screen: Screen, clear: Boolean) {
        navController.navigate(screen()) {
            if (clear) popUpTo(0)
        }
    }

    fun onNavigateBack() {
        if (navController.previousBackStackEntry != null) {
            navController.navigateUp()
        } else {
            navController.navigate(Screen.SignIn()) {
                popUpTo(0)
            }
        }
    }

    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(Screen.SignIn()) {
            AuthScreen(
                onNavigate = ::onNavigate, screen = Screen.SignIn, onEvent = onEvent, state = state
            )
        }
        composable(Screen.SignUp()) {
            AuthScreen(
                onNavigate = ::onNavigate, screen = Screen.SignUp, onEvent = onEvent, state = state
            )
        }
    }
}
