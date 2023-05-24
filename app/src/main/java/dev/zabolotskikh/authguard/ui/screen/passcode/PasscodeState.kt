package dev.zabolotskikh.authguard.ui.screen.passcode

data class PasscodeState(
    val isConfirmed: Boolean = false,
    val isLimitReached: Boolean = false,
    val isSucceed: Boolean = false,
    val isCancelled: Boolean = false,
    val passcode: String = "",
    val attemptCount: Int = 0
)
