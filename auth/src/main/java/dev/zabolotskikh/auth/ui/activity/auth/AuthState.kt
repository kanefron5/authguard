package dev.zabolotskikh.auth.ui.activity.auth

data class AuthState(
    val email: String = "",
    val password: String = "",
    val isPasswordValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isResetPasswordDialogShown: Boolean = false,
    val isProgress: Boolean = false,
    val error: Exception? = null
)
