package dev.zabolotskikh.authguard.ui.screen.auth

sealed interface AuthEvent {
    object OnForgotPassword: AuthEvent
    object OnSignUp: AuthEvent
    object OnSignIn: AuthEvent

    data class OnEditPassword(val value: String): AuthEvent
    data class OnEditEmail(val value: String): AuthEvent
}