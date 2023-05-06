package dev.zabolotskikh.authentificator.domain.model

data class AppState(
    val isStarted: Boolean,
    val isAuthenticated: Boolean
)
