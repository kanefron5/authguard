package dev.zabolotskikh.auth.ui.activity.auth.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.auth.R
import dev.zabolotskikh.auth.ui.activity.Screen
import dev.zabolotskikh.auth.ui.activity.auth.AuthEvent
import dev.zabolotskikh.auth.ui.activity.auth.AuthState
import dev.zabolotskikh.auth.ui.activity.auth.components.LabeledTextField
import dev.zabolotskikh.auth.ui.activity.auth.components.ProgressButton
import dev.zabolotskikh.auth.ui.preview.provider.FakeAuthStateProvider

private const val SIGN_UP_TAG = "sign_up"

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onEvent: (AuthEvent) -> Unit, state: AuthState,
    onNavigate: (screen: Screen, clear: Boolean) -> Unit = { _, _ -> }
) {
    if (state.isResetPasswordDialogShown) {
        ResetPasswordDialog(state = state, onEvent = onEvent)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(PaddingValues(horizontal = 16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.auth_welcome_back),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 26.sp,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.auth_signin_to_account),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 17.sp,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column {
            LabeledTextField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.auth_placeholder_email),
                value = state.email,
                isValid = state.isEmailValid,
                onValueChanged = { onEvent(AuthEvent.OnEditEmail(it)) },
                type = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(8.dp))
            LabeledTextField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.auth_placeholder_password),
                value = state.password,
                type = KeyboardType.Password,
                onValueChanged = { onEvent(AuthEvent.OnEditPassword(it)) },
                isValid = state.isPasswordValid
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {
                onEvent(AuthEvent.OnForgotPasswordDialog(true))
            }) {
                Text(text = stringResource(id = R.string.auth_forgot_password))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ProgressButton(
            enabled = state.isEmailValid && state.isPasswordValid && state.email.isNotBlank() && state.password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = { onEvent(AuthEvent.OnSignIn) },
            text = stringResource(id = R.string.action_continue),
            isProgress = state.isProgress
        )

        val signUpText = buildAnnotatedString {
            append(stringResource(id = R.string.auth_no_account))
            append(" ")
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                pushStringAnnotation(SIGN_UP_TAG, "")
                append(stringResource(id = R.string.signup))
                pop()
            }
        }
        ClickableText(
            modifier = Modifier.padding(24.dp),
            text = signUpText,
            onClick = { offset ->
                signUpText.getStringAnnotations(SIGN_UP_TAG, offset, offset).firstOrNull()?.let {
                    onNavigate(Screen.SignUp, false)
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun SignInScreenPreview(
    @PreviewParameter(FakeAuthStateProvider::class) authState: AuthState
) {
    SignInScreen(onEvent = {}, state = authState)
}