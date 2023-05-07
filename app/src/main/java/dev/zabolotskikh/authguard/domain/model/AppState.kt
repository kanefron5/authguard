package dev.zabolotskikh.authguard.domain.model

data class AppState(
    val isStarted: Boolean,
    val isAuthenticated: Boolean
)
