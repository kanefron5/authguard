package dev.zabolotskikh.authguard.ui.screen.settings.sections.main.account

sealed interface AccountEvent {
    object OnVerifyEmail: AccountEvent
    object OnSignOut: AccountEvent
}