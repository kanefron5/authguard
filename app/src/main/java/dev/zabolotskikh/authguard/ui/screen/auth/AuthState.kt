package dev.zabolotskikh.authguard.ui.screen.auth

data class AuthState(
    val email: String = "",
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val isEmailValid: Boolean = true
)
