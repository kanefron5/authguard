package dev.zabolotskikh.authguard.domain.model

data class AppState(
    val isStarted: Boolean = false,
    val isRemoteMode: Boolean = false,
    val isPrivateMode: Boolean = false,
    val passcode: Passcode? = null
)
