package dev.zabolotskikh.auth.ui.provider

import dev.zabolotskikh.authguard.domain.model.UserAccount

internal data class ProviderState(
    val userAccount: UserAccount? = null,
    val isAuthenticated: Boolean = userAccount != null,
)
