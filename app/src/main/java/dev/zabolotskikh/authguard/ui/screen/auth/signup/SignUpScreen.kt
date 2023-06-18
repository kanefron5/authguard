package dev.zabolotskikh.authguard.ui.screen.auth.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.zabolotskikh.authguard.R
import dev.zabolotskikh.authguard.ui.preview.providers.FakeAuthStateProvider
import dev.zabolotskikh.authguard.ui.screen.auth.AuthEvent
import dev.zabolotskikh.authguard.ui.screen.auth.AuthState
import dev.zabolotskikh.authguard.ui.screen.auth.components.LabeledTextField
import dev.zabolotskikh.authguard.ui.screen.auth.components.TextFieldType

private const val PRIVACY_POLICY_TAG = "privacy_policy"
private const val TERMS_OF_USE_TAG = "terms_of_use"

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onEvent: (AuthEvent) -> Unit,
    state: AuthState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(PaddingValues(horizontal = 16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.signup),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 26.sp,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(id = R.string.auth_signup_message),
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
                type = TextFieldType.EMAIL
            )
            Spacer(modifier = Modifier.height(8.dp))
            LabeledTextField(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.auth_placeholder_password),
                value = state.password,
                type = TextFieldType.PASSWORD,
                onValueChanged = { onEvent(AuthEvent.OnEditPassword(it)) },
                isValid = state.isPasswordValid
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.auth_password_requirements),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.secondary),
                fontSize = 10.sp,
                textAlign = TextAlign.Justify
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = state.isEmailValid && state.isPasswordValid && state.email.isNotBlank() && state.password.isNotBlank(),
            shape = RoundedCornerShape(8.dp),
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(text = stringResource(id = R.string.auth_create_account))
        }

        val text = buildAnnotatedString {
            append(stringResource(id = R.string.auth_signup_text))
            append(" ")
            val style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
            withStyle(style) {
                this.pushStringAnnotation(TERMS_OF_USE_TAG, "")
                append(stringResource(id = R.string.auth_terms_of_use))
                pop()
            }
            append(" ")
            append(stringResource(id = R.string.and))
            append(" ")
            withStyle(style) {
                this.pushStringAnnotation(PRIVACY_POLICY_TAG, "")
                append(stringResource(id = R.string.auth_privacy_policy))
                pop()
            }
        }
        ClickableText(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(.7f),
            text = text,
            onClick = { offset ->
                text.getStringAnnotations(TERMS_OF_USE_TAG, offset, offset).firstOrNull()?.let {
                    // TODO
                }
                text.getStringAnnotations(PRIVACY_POLICY_TAG, offset, offset).firstOrNull()?.let {
                    // TODO
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.secondary)
        )
    }
}

@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
private fun SignUpScreenPreview(
    @PreviewParameter(FakeAuthStateProvider::class) authState: AuthState
) {
    SignUpScreen(onEvent = {}, state = authState)
}