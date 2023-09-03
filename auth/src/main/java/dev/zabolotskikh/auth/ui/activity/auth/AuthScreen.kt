package dev.zabolotskikh.auth.ui.activity.auth

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.zabolotskikh.auth.R
import dev.zabolotskikh.auth.ui.activity.Screen
import dev.zabolotskikh.auth.ui.activity.auth.components.AuthErrorDialog
import dev.zabolotskikh.auth.ui.activity.auth.signin.SignInScreen
import dev.zabolotskikh.auth.ui.activity.auth.signup.SignUpScreen

// https://dribbble.com/shots/11693634-Onboarding-UI-Kit-Preview
@Composable
fun AuthScreen(
    onNavigate: (screen: Screen, clear: Boolean) -> Unit = { _, _ -> },
    screen: Screen
) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val state by viewModel.state.collectAsState()

    AuthScreenView(
        onEvent = viewModel::onEvent,
        state = state,
        onNavigate = onNavigate,
        screen = screen
    )
}

@Composable
private fun AuthScreenView(
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    screen: Screen,
    onNavigate: (screen: Screen, clear: Boolean) -> Unit = { _, _ -> }
) {
    state.error?.let { error ->
        AuthErrorDialog(exception = error, onDismiss = {
            onEvent(AuthEvent.OnDismissError)
        })
    }

    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            val onBackPressedDispatcher =
                LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
            IconButton(onClick = { onBackPressedDispatcher?.onBackPressed() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back")
            }
            Text(text = stringResource(id = R.string.back))
        }

        when (screen) {
            Screen.SignIn -> {
                SignInScreen(
                    modifier = Modifier.padding(top = 16.dp),
                    onEvent = onEvent,
                    state = state,
                    onNavigate = onNavigate
                )
            }

            Screen.SignUp -> {
                SignUpScreen(
                    modifier = Modifier.padding(top = 16.dp),
                    onEvent = onEvent,
                    state = state,
                )
            }
        }
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun AuthScreenPreview() {
    AuthScreenView(onEvent = {}, state = AuthState(), screen = Screen.SignUp)
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun AuthScreenPreview2() {
    AuthScreenView(onEvent = {}, state = AuthState(), screen = Screen.SignIn)
}