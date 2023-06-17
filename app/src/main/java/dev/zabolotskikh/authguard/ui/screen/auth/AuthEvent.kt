package dev.zabolotskikh.authguard.ui.screen.auth

sealed interface AuthEvent {
    object StartLocal: AuthEvent
}