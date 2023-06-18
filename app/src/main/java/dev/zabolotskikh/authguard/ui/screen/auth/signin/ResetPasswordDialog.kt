package dev.zabolotskikh.authguard.ui.screen.auth.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.ui.preview.providers.FakeAuthStateProvider
import dev.zabolotskikh.authguard.ui.screen.auth.AuthEvent
import dev.zabolotskikh.authguard.ui.screen.auth.AuthState
import dev.zabolotskikh.authguard.ui.screen.auth.components.LabeledTextField
import dev.zabolotskikh.authguard.ui.screen.auth.components.TextFieldType

@Composable
fun ResetPasswordDialog(
    modifier: Modifier = Modifier,
    state: AuthState,
    onEvent: (AuthEvent) -> Unit = {},
) {
    AlertDialog(onDismissRequest = { },
        title = { Text(text = stringResource(id = R.string.auth_password_recovery_title)) },
        text = {
            Column {
                Text(text = stringResource(id = R.string.auth_password_recovery_message))
                LabeledTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    label = stringResource(id = R.string.auth_placeholder_email),
                    value = state.email,
                    isValid = state.isEmailValid,
                    onValueChanged = { onEvent(AuthEvent.OnEditEmail(it)) },
                    type = TextFieldType.EMAIL
                )
            }
        },
        confirmButton = {
            Button(enabled = state.isEmailValid && state.email.isNotBlank(),
                onClick = { onEvent(AuthEvent.OnForgotPassword) }) {
                Text(text = stringResource(id = R.string.send))
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(AuthEvent.OnForgotPasswordDialog(false)) }) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun ResetPasswordDialogPreview(
    @PreviewParameter(FakeAuthStateProvider::class) authState: AuthState
) {
    ResetPasswordDialog(state = authState)
}