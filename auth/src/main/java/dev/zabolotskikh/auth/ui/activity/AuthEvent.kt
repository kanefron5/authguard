package dev.zabolotskikh.auth.ui.activity

internal sealed interface AuthEvent {
    object OnForgotPassword: AuthEvent
    data class OnForgotPasswordDialog(val isShown: Boolean): AuthEvent
    object OnSignUp: AuthEvent
    object OnSignIn: AuthEvent
    data class OnEditPassword(val value: String): AuthEvent
    data class OnEditEmail(val value: String): AuthEvent
    object OnDismissError: AuthEvent
}