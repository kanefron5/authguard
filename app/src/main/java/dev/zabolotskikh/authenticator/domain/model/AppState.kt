package dev.zabolotskikh.authenticator.domain.model

data class AppState(
    val isStarted: Boolean,
    val isAuthenticated: Boolean
)
