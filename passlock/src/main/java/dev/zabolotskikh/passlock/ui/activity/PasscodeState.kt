package dev.zabolotskikh.passlock.ui.activity

internal data class PasscodeState(
    val isConfirmed: Boolean = false,
    val isLimitReached: Boolean = false,
    val isRejected: Boolean = false,
    val isSucceed: Boolean = false,
    val isCancelled: Boolean = false,
    val passcode: String = "",
    val attemptCount: Int = 0
)

internal fun PasscodeState.reset() = copy(
    isConfirmed = false,
    isLimitReached = false,
    isSucceed = false,
    isCancelled = false,
    isRejected = false
)
