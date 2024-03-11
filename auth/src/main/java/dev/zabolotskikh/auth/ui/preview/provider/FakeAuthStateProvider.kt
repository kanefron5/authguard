package dev.zabolotskikh.auth.ui.preview.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.zabolotskikh.auth.ui.activity.AuthState

class FakeAuthStateProvider : PreviewParameterProvider<AuthState> {
    override val values: Sequence<AuthState>
        get() = sequenceOf(
            AuthState(),
            AuthState(isResetPasswordDialogShown = true),
            AuthState("example@zabolotskikh.dev", "##qwertYUIOP123@@"),
            AuthState("example@zabolotskikh", "##qwertYUIOP123@@", isEmailValid = false),
            AuthState("example@zabolotskikh.dev", "12345", isPasswordValid = false),
            AuthState("example@zabolotskikh", "12345", isPasswordValid = false, isEmailValid = false),
        )
}