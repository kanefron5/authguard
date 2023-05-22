package dev.zabolotskikh.authguard.domain.model

data class AppState(
    val isStarted: Boolean,
    val isRemoteMode: Boolean,
    val isPrivateMode: Boolean,
    val passcode: Passcode? = null
)
